package xyz.datt.domain.place.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.place.dto.PlaceNearbyCondition;
import xyz.datt.domain.place.dto.PlaceNearbyResponse;
import xyz.datt.domain.place.entity.PlaceMaster;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PlaceQueryRepositoryTest {
    @Autowired
    private PlaceMasterRepository placeMasterRepository;

    @Test
    @DisplayName("기준 좌표 반경 내 장소를 조회한다.")
    void givenCoordinateAndRadius_whenSearchNearbyPlaces_thenReturnPlaces() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스 강남점",
            "I212",
            "비알코올",
            127.0276,
            37.4979
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "부산 카페",
            "I212",
            "비알코올",
            129.0756,
            35.1796
        ));

        PlaceNearbyCondition condition = new PlaceNearbyCondition();
        condition.setLon(127.0276);
        condition.setLat(37.4979);
        condition.setRadiusKm(1.0);

        Page<PlaceNearbyResponse> result = placeMasterRepository.searchNearbyPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).bizesNm()).isEqualTo("스타벅스 강남점");
        assertThat(result.getContent().get(0).distanceKm()).isLessThanOrEqualTo(1.0);
    }

    @Test
    @DisplayName("거리순으로 주변 장소를 조회한다.")
    void givenCoordinate_whenSearchNearbyPlaces_thenReturnPlacesOrderByDistance() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "가까운 카페",
            "I212",
            "비알코올",
            127.0276,
            37.4979
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "조금 먼 카페",
            "I212",
            "비알코올",
            127.0400,
            37.5050
        ));

        PlaceNearbyCondition condition = new PlaceNearbyCondition();
        condition.setLon(127.0276);
        condition.setLat(37.4979);
        condition.setRadiusKm(5.0);

        Page<PlaceNearbyResponse> result = placeMasterRepository.searchNearbyPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).bizesNm()).isEqualTo("가까운 카페");
        assertThat(result.getContent().get(0).distanceKm())
            .isLessThanOrEqualTo(result.getContent().get(1).distanceKm());
    }

    @Test
    @DisplayName("업종 중분류 코드로 주변 장소를 필터링한다.")
    void givenIndustryCategory_whenSearchNearbyPlaces_thenReturnFilteredPlaces() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스 강남점",
            "I212",
            "비알코올",
            127.0276,
            37.4979
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "한식당 강남점",
            "I201",
            "한식",
            127.0277,
            37.4980
        ));

        PlaceNearbyCondition condition = new PlaceNearbyCondition();
        condition.setLon(127.0276);
        condition.setLat(37.4979);
        condition.setRadiusKm(3.0);
        condition.setIndsMclsCd("I212");

        Page<PlaceNearbyResponse> result = placeMasterRepository.searchNearbyPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).indsMclsCd()).isEqualTo("I212");
    }

    @Test
    @DisplayName("키워드로 주변 장소를 필터링한다.")
    void givenKeyword_whenSearchNearbyPlaces_thenReturnFilteredPlaces() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스 강남점",
            "I212",
            "비알코올",
            127.0276,
            37.4979
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "메가커피 강남점",
            "I212",
            "비알코올",
            127.0277,
            37.4980
        ));

        PlaceNearbyCondition condition = new PlaceNearbyCondition();
        condition.setLon(127.0276);
        condition.setLat(37.4979);
        condition.setRadiusKm(3.0);
        condition.setKeyword("스타벅스");

        Page<PlaceNearbyResponse> result = placeMasterRepository.searchNearbyPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).bizesNm()).contains("스타벅스");
    }

    private PlaceMaster createPlaceMaster(
        String bizesId,
        String bizesNm,
        String indsMclsCd,
        String indsMclsNm,
        Double lon,
        Double lat
    ) {
        return PlaceMaster.builder()
            .bizesId(bizesId)
            .bizesNm(bizesNm)
            .brchNm(null)
            .indsLclsCd("I2")
            .indsLclsNm("음식")
            .indsMclsCd(indsMclsCd)
            .indsMclsNm(indsMclsNm)
            .indsSclsCd("I21201")
            .indsSclsNm("카페")
            .ctprvnNm("서울특별시")
            .signguNm("강남구")
            .adongNm("역삼동")
            .ldongNm("역삼동")
            .lnoAdr("서울특별시 강남구 역삼동")
            .rdnmAdr("서울특별시 강남구 테헤란로")
            .newZipcd("06123")
            .lon(lon)
            .lat(lat)
            .location("POINT(" + lon + " " + lat + ")")
            .build();
    }
}