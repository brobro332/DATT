package xyz.datt.domain.place.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import xyz.datt.domain.place.dto.PlaceNearbyCondition;
import xyz.datt.domain.place.repository.PlaceMasterRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class PlaceNearbyServiceTest {
    private final PlaceMasterRepository placeMasterRepository = mock(PlaceMasterRepository.class);

    private final PlaceNearbyService placeNearbyService =
        new PlaceNearbyService(placeMasterRepository);

    @Test
    @DisplayName("위도 또는 경도가 없으면 예외가 발생한다.")
    void givenNullCoordinate_whenSearchNearbyPlaces_thenThrowException() {
        PlaceNearbyCondition condition = new PlaceNearbyCondition();
        condition.setRadiusKm(3.0);

        assertThatThrownBy(() ->
            placeNearbyService.searchNearbyPlaces(condition, PageRequest.of(0, 10))
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("반경이 0 이하이면 예외가 발생한다.")
    void givenInvalidRadius_whenSearchNearbyPlaces_thenThrowException() {
        PlaceNearbyCondition condition = new PlaceNearbyCondition();
        condition.setLon(127.0276);
        condition.setLat(37.4979);
        condition.setRadiusKm(0.0);

        assertThatThrownBy(() ->
            placeNearbyService.searchNearbyPlaces(condition, PageRequest.of(0, 10))
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("위도 범위를 벗어나면 예외가 발생한다.")
    void givenInvalidLatitude_whenSearchNearbyPlaces_thenThrowException() {
        PlaceNearbyCondition condition = new PlaceNearbyCondition();
        condition.setLon(127.0276);
        condition.setLat(100.0);
        condition.setRadiusKm(3.0);

        assertThatThrownBy(() ->
            placeNearbyService.searchNearbyPlaces(condition, PageRequest.of(0, 10))
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("경도 범위를 벗어나면 예외가 발생한다.")
    void givenInvalidLongitude_whenSearchNearbyPlaces_thenThrowException() {
        PlaceNearbyCondition condition = new PlaceNearbyCondition();
        condition.setLon(200.0);
        condition.setLat(37.4979);
        condition.setRadiusKm(3.0);

        assertThatThrownBy(() ->
            placeNearbyService.searchNearbyPlaces(condition, PageRequest.of(0, 10))
        ).isInstanceOf(RuntimeException.class);
    }
}