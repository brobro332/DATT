package xyz.datt.domain.place.batch.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.datt.domain.place.dto.PlaceApiResponse;
import xyz.datt.domain.place.dto.PlaceResponseDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PlaceApiItemReader implements ItemReader<PlaceResponseDto> {
    private final WebClient webClient;

    @Value("${data.path}")
    private String path;

    @Value("${data.service-key}")
    private String serviceKey;

    @Value("${data.div-id}")
    private String divId;

    @Value("${data.key}")
    private String key;

    private int pageNo = 1;
    private final int numOfRows = 1000;
    private List<PlaceResponseDto> results = new ArrayList<>();
    private int nextIndex = 0;
    private boolean isExhausted = false;

    public PlaceApiItemReader(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public PlaceResponseDto read() {
        if (nextIndex >= results.size()) {
            if (isExhausted) return null;

            fetchNextPage();
            nextIndex = 0;

            if (results.isEmpty()) return null;
        }

        return results.get(nextIndex++);
    }

    private void fetchNextPage() {
        log.info("Place API 호출 중... [페이지: {}]", pageNo);

        try {
            PlaceApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path(path)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("pageNo", pageNo)
                    .queryParam("numOfRows", numOfRows)
                    .queryParam("type", "json")
                    .queryParam("divId", divId)
                    .queryParam("key", key)
                    .build())
                .retrieve()
                .bodyToMono(PlaceApiResponse.class)
                .block();

            if (response != null &&
                response.getBody() != null &&
                response.getBody().getItems() != null &&
                !response.getBody().getItems().isEmpty()) {
                this.results = response.getBody().getItems();
                this.pageNo++;
            } else {
                log.info("더 이상 가져올 데이터가 없습니다. 조회를 종료합니다.");
                this.isExhausted = true;
            }
        } catch (Exception e) {
            log.error("API 호출 중 에러 발생: {}", e.getMessage());
            this.isExhausted = true;
        }
    }
}