package xyz.datt.domain.place.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.datt.domain.place.dto.PlaceResponseDto;
import xyz.datt.domain.place.dto.SearchTask;
import xyz.datt.domain.place.entity.KeywordHistory;
import xyz.datt.domain.place.entity.Place;
import xyz.datt.domain.place.entity.Platform;
import xyz.datt.domain.place.mapper.PlaceMapper;
import xyz.datt.domain.place.repository.KeywordHistoryRepository;
import xyz.datt.domain.place.repository.PlaceQueryRepository;
import xyz.datt.domain.place.repository.PlaceRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final KeywordHistoryRepository historyRepository;
    private final PlaceRepository placeRepository;
    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceMapper placeMapper;
    private final WebClient agentWebClient;

    public Page<PlaceResponseDto> getPlacesPage(String keyword, String category, Platform platform, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return placeQueryRepository.searchPlaces(null, category, platform, pageable)
                .map(placeMapper::toResponseDto);
        }

        List<PlaceResponseDto> allResults = getPlacesFromAllSources(keyword);

        if (category != null && !category.isEmpty() && !category.equals("전체")) {
            allResults = allResults.stream()
                .filter(p -> category.equals(p.getCategory()))
                .collect(Collectors.toList());
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allResults.size());

        List<PlaceResponseDto> content = Collections.emptyList();
        if (start < allResults.size()) {
            content = allResults.subList(start, end);
        }

        return new PageImpl<>(content, pageable, allResults.size());
    }

    public List<PlaceResponseDto> getPlacesFromAllSources(String keyword) {
        List<String> categories = Arrays.asList("맛집", "카페", "숙소", "술집");
        List<Platform> platforms = Arrays.asList(Platform.NAVER, Platform.GOOGLE);

        List<SearchTask> tasks = new ArrayList<>();
        for (Platform p : platforms) {
            for (String c : categories) {
                if (p == Platform.GOOGLE && c.equals("숙소")) continue;
                tasks.add(new SearchTask(p, c));
            }
        }
        Collections.shuffle(tasks);

        List<CompletableFuture<List<PlaceResponseDto>>> futures = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            SearchTask task = tasks.get(i);
            long delay = i * 1500L + (long)(Math.random() * 1000);
            futures.add(processTask(keyword, task, delay));
        }

        return futures.stream()
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .collect(Collectors.toMap(
                PlaceResponseDto::getPlaceUrl,
                dto -> dto,
                (existing, replacement) -> existing
            ))
            .values()
            .stream()
            .sorted(Comparator.comparing(PlaceResponseDto::getVisitorReviewCount,
                Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());
    }

    public CompletableFuture<List<PlaceResponseDto>> processTask(String keyword, SearchTask task, long delay) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<KeywordHistory> history =
                historyRepository.findByKeywordAndCategoryAndPlatform(keyword, task.category(), task.platform());

            if (isDataFresh(history, task.category(), task.platform())) {
                log.info("캐시 적중 [{} - {}]", task.platform(), task.category());

                return placeQueryRepository.searchPlaces(keyword, task.category(), task.platform(), Pageable.unpaged())
                    .stream()
                    .map(placeMapper::toResponseDto)
                    .collect(Collectors.toList());
            }

            try { Thread.sleep(delay); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            List<PlaceResponseDto> scrapedData = callAgent(keyword, task.category(), task.platform());
            scrapedData.forEach(dto -> dto.setCategory(task.category()));

            saveData(keyword, task, scrapedData, history);

            return scrapedData;
        });
    }

    @Transactional
    protected synchronized void saveData(String keyword, SearchTask task, List<PlaceResponseDto> scrapedData, Optional<KeywordHistory> history) {
        if (scrapedData == null || scrapedData.isEmpty()) return;

        List<String> targetUrls = scrapedData.stream()
            .map(PlaceResponseDto::getPlaceUrl)
            .collect(Collectors.toList());

        Map<String, Place> existingPlaceMap = placeRepository.findByPlaceUrlIn(targetUrls)
            .stream()
            .collect(Collectors.toMap(Place::getPlaceUrl, p -> p, (oldValue, newValue) -> oldValue));

        List<Place> toSave = new ArrayList<>();

        for (PlaceResponseDto dto : scrapedData) {
            if (existingPlaceMap.containsKey(dto.getPlaceUrl())) {
                Place existingPlace = existingPlaceMap.get(dto.getPlaceUrl());
                existingPlace.updateInfo(dto.getName(), dto.getVisitorReviewCount(), dto.getImageUrls(), dto.getRating());
                toSave.add(existingPlace);
            } else {
                toSave.add(placeMapper.toEntity(dto, keyword, task.category(), task.platform()));
            }
        }

        placeRepository.saveAll(toSave);

        KeywordHistory h = history.orElseGet(() -> new KeywordHistory(keyword, task.category(), task.platform()));
        h.updateTimestamp();
        historyRepository.save(h);
    }

    private List<PlaceResponseDto> callAgent(String keyword, String category, Platform platform) {
        return agentWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/crawl/{provider}")
                .queryParam("keyword", keyword)
                .queryParam("category", category)
                .build(platform.getCode().toLowerCase()))
            .retrieve()
            .bodyToFlux(PlaceResponseDto.class)
            .collectList()
            .timeout(Duration.ofSeconds(60))
            .onErrorReturn(Collections.emptyList())
            .block();
    }

    private boolean isDataFresh(Optional<KeywordHistory> history, String category, Platform platform) {
        if (history.isEmpty()) return false;
        long daysPassed = ChronoUnit.DAYS.between(history.get().getLastScrapedAt(), LocalDateTime.now());
        int threshold = category.equals("숙소") ? 7 : 3;
        return daysPassed < threshold;
    }
}