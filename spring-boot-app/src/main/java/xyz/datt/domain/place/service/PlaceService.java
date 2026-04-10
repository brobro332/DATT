package xyz.datt.domain.place.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PlaceMapper placeMapper;
    private final WebClient agentWebClient;

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
                .collect(Collectors.toList());
    }

    private CompletableFuture<List<PlaceResponseDto>> processTask(String keyword, SearchTask task, long delay) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<KeywordHistory> history =
                historyRepository.findByKeywordAndCategoryAndPlatform(keyword, task.category(), task.platform());

            if (isDataFresh(history, task.category(), task.platform())) {
                log.info("캐시 적중 [{} - {}]", task.platform(), task.category());
                return placeRepository.findByKeywordAndCategoryAndPlatform(keyword, task.category(), task.platform())
                    .stream().map(placeMapper::toResponseDto).collect(Collectors.toList());
            }

            try { Thread.sleep(delay); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            List<PlaceResponseDto> scrapedData = callAgent(keyword, task.category(), task.platform());
            saveData(keyword, task, scrapedData, history);

            return scrapedData;
        });
    }

    @Transactional
    protected void saveData(String keyword, SearchTask task, List<PlaceResponseDto> scrapedData, Optional<KeywordHistory> history) {
        if (scrapedData == null || scrapedData.isEmpty()) {
            log.warn("에이전트로부터 받은 데이터가 없습니다. 저장 생략 [{}]", task);
            return;
        }

        try {
            placeRepository.deleteOldData(keyword, task.category(), task.platform());
            placeRepository.flush();

            List<Place> entities = scrapedData.stream()
                    .map(dto -> placeMapper.toEntity(dto, keyword, task.category(), task.platform()))
                    .collect(Collectors.toList());

            placeRepository.saveAll(entities);
            placeRepository.flush();

            KeywordHistory h = history.orElseGet(() -> new KeywordHistory(keyword, task.category(), task.platform()));
            h.updateTimestamp();
            historyRepository.save(h);

            log.info("성공적으로 저장됨 [{}건 ({} - {})]", entities.size(), task.platform(), task.category());
        } catch (Exception e) {
            log.error("데이터 저장 중 오류 발생 [{}]", e.getMessage(), e);
        }
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

        int threshold;
        if (category.equals("숙소")) {
            threshold = 7;
        } else {
            threshold = 3;
        }

        return daysPassed < threshold;
    }
}
