package xyz.datt.domain.anchor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.anchor.dto.AnchorResponseDto;
import xyz.datt.domain.anchor.entity.AnchorSnapshot;
import xyz.datt.domain.anchor.mapper.AnchorMapper;
import xyz.datt.domain.anchor.repository.AnchorRepository;
import xyz.datt.domain.place.dto.PlaceResponseDto;
import xyz.datt.domain.place.dto.SearchTask;
import xyz.datt.domain.place.entity.Platform;
import xyz.datt.domain.place.service.PlaceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnchorService {
    private final PlaceService placeService;
    private final AnchorRepository anchorRepository;
    private final AnchorMapper anchorMapper;
    private final ObjectMapper objectMapper;

    // 데이터 재사용 기간 (7일)
    private static final long CACHE_DAYS = 7;

    @Transactional
    public AnchorResponseDto createAnchor(String keyword) throws Exception {
        // 1. 키워드 정규화 (앞뒤 공백 제거)
        String normalizedKeyword = keyword.trim();

        // 2. 효율화 로직: 기존에 생성된 동일 키워드의 닻이 있는지 확인
        Optional<AnchorSnapshot> existingAnchor = anchorRepository.findFirstByKeywordOrderByCreatedAtDesc(normalizedKeyword);

        if (existingAnchor.isPresent()) {
            AnchorSnapshot snapshot = existingAnchor.get();
            // 7일 이내에 생성된 데이터라면 크롤링 없이 바로 반환
            if (snapshot.getCreatedAt().isAfter(LocalDateTime.now().minusDays(CACHE_DAYS))) {
                return anchorMapper.toResponse(snapshot);
            }
        }

        // 3. 신규 생성 로직 (기존 데이터가 없거나 오래된 경우)
        List<Platform> platforms = List.of(Platform.NAVER, Platform.GOOGLE);
        List<String> categories = List.of("맛집", "카페", "숙소", "술집");

        List<CompletableFuture<MapEntry>> futures = new ArrayList<>();

        for (Platform platform : platforms) {
            for (String category : categories) {
                // 구글은 숙소 카테고리 제외 (크롤링 효율성)
                if (platform == Platform.GOOGLE && "숙소".equals(category)) continue;

                SearchTask task = new SearchTask(platform, category);

                // PlaceService 비동기 호출
                CompletableFuture<MapEntry> future = placeService.processTask(normalizedKeyword, task, 0L)
                        .thenApply(places -> new MapEntry(platform.name(), category, sortByWeightedScore(places)));

                futures.add(future);
            }
        }

        // 비동기 작업 완료 대기 및 결과 조립
        Map<String, Map<String, List<PlaceResponseDto>>> content = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.groupingBy(
                        MapEntry::platform,
                        Collectors.toMap(MapEntry::category, MapEntry::places)
                ));

        // 4. JSON 변환 및 저장
        String json = objectMapper.writeValueAsString(content);
        AnchorSnapshot saved = anchorRepository.save(new AnchorSnapshot(normalizedKeyword, json));

        return anchorMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AnchorResponseDto getAnchor(String anchorId) {
        AnchorSnapshot snapshot = anchorRepository.findById(anchorId)
                .orElseThrow(() -> new RuntimeException("해당 닻을 찾을 수 없습니다. ID: " + anchorId));

        return anchorMapper.toResponse(snapshot);
    }

    private List<PlaceResponseDto> sortByWeightedScore(List<PlaceResponseDto> places) {
        return places.stream()
                .sorted((p1, p2) -> {
                    double s1 = calculateScore(p1.getRating(), p1.getVisitorReviewCount());
                    double s2 = calculateScore(p2.getRating(), p2.getVisitorReviewCount());
                    return Double.compare(s2, s1);
                })
                .limit(5)
                .toList();
    }

    private double calculateScore(Double rating, Integer reviewCount) {
        double r = (rating != null) ? rating : 0.0;
        double c = (reviewCount != null) ? (double) reviewCount : 0.0;
        return (r * 7.0) + (Math.log10(c + 1) * 3.0);
    }

    private record MapEntry(String platform, String category, List<PlaceResponseDto> places) {}
}