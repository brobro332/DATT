package xyz.datt.domain.place.batch.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import xyz.datt.domain.place.batch.config.PlacePublicDataProperties;
import xyz.datt.domain.place.batch.dto.PlacePublicDataItem;
import xyz.datt.domain.place.batch.dto.PlacePublicDataResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlacePublicDataClient {

    private final RestClient restClient;
    private final PlacePublicDataProperties properties;

    public List<PlacePublicDataItem> fetchPlaces(
        String divId,
        String key,
        int pageNo
    ) {
        PlacePublicDataResponse response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host("apis.data.go.kr")
                .path(properties.getEndpoint())
                .queryParam("ServiceKey", properties.getServiceKey())
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", properties.getNumOfRows())
                .queryParam("divId", divId)
                .queryParam("key", key)
                .queryParam("type", properties.getType())
                .build()
            )
            .retrieve()
            .body(PlacePublicDataResponse.class);

        return Optional.ofNullable(response)
            .map(PlacePublicDataResponse::getBody)
            .map(PlacePublicDataResponse.Body::getItems)
            .orElse(Collections.emptyList());
    }
}