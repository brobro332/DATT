package xyz.datt.domain.place.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.place.dto.PlaceSearchCondition;
import xyz.datt.domain.place.dto.PlaceSearchResponse;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.entity.PlaceSortType;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PlaceMasterRepositoryTest {
    @Autowired
    private PlaceMasterRepository placeMasterRepository;

    @Test
    @DisplayName("PlaceMaster 저장에 성공한다.")
    void givenPlaceMaster_whenSave_thenPersistPlaceMaster() {
        PlaceMaster placeMaster = createPlaceMaster("TEST-001", "스타벅스 강남점");

        PlaceMaster savedPlaceMaster = placeMasterRepository.save(placeMaster);

        assertThat(savedPlaceMaster.getId()).isNotNull();
        assertThat(savedPlaceMaster.getBizesId()).isEqualTo("TEST-001");
        assertThat(savedPlaceMaster.getBizesNm()).isEqualTo("스타벅스 강남점");
    }

    @Test
    @DisplayName("상가업소번호로 존재 여부를 확인한다.")
    void givenBizesId_whenExistsByBizesId_thenReturnTrue() {
        placeMasterRepository.save(createPlaceMaster("TEST-001", "스타벅스 강남점"));

        boolean exists = placeMasterRepository.existsByBizesId("TEST-001");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("상가업소번호로 PlaceMaster를 조회한다.")
    void givenBizesId_whenFindByBizesId_thenReturnPlaceMaster() {
        placeMasterRepository.save(createPlaceMaster("TEST-001", "스타벅스 강남점"));

        Optional<PlaceMaster> placeMaster =
            placeMasterRepository.findByBizesId("TEST-001");

        assertThat(placeMaster).isPresent();
        assertThat(placeMaster.get().getBizesNm())
            .isEqualTo("스타벅스 강남점");
    }

    @Test
    @DisplayName("상호명 키워드로 PlaceMaster를 검색한다.")
    void givenKeyword_whenFindByBizesNmContaining_thenReturnPlaceMasters() {
        placeMasterRepository.save(createPlaceMaster("TEST-001", "스타벅스 강남점"));

        placeMasterRepository.save(createPlaceMaster("TEST-002", "메가커피 선릉점"));

        List<PlaceMaster> result =
            placeMasterRepository.findByBizesNmContaining("스타벅스");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBizesNm())
            .isEqualTo("스타벅스 강남점");
    }

    private PlaceMaster createPlaceMaster(String bizesId, String bizesNm) {
        return PlaceMaster.builder()
            .bizesId(bizesId)
            .bizesNm(bizesNm)
            .brchNm(null)
            .indsLclsCd("I2")
            .indsLclsNm("음식")
            .indsMclsCd("I201")
            .indsMclsNm("카페")
            .indsSclsCd("I20101")
            .indsSclsNm("커피전문점/카페")
            .ctprvnNm("서울특별시")
            .signguNm("강남구")
            .adongNm("역삼동")
            .ldongNm("역삼동")
            .lnoAdr("서울특별시 강남구 역삼동 123-1")
            .rdnmAdr("서울특별시 강남구 테헤란로 123")
            .newZipcd("06123")
            .lon(127.0276)
            .lat(37.4979)
            .location("POINT(127.0276 37.4979)")
            .build();
    }

    @Test
    @DisplayName("키워드로 장소를 검색한다.")
    void givenKeyword_whenSearchPlaces_thenReturnPlaces() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스 강남점",
            "강남점",
            "서울특별시",
            "강남구",
            "역삼동",
            "I212",
            "비알코올"
        ));

        PlaceSearchCondition condition = new PlaceSearchCondition();
        condition.setKeyword("스타벅스");

        Page<PlaceSearchResponse> result = placeMasterRepository.searchPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).bizesNm()).isEqualTo("스타벅스 강남점");
    }

    @Test
    @DisplayName("지역 조건으로 장소를 검색한다.")
    void givenRegionCondition_whenSearchPlaces_thenReturnPlaces() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스 강남점",
            "강남점",
            "서울특별시",
            "강남구",
            "역삼동",
            "I212",
            "비알코올"
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "메가커피 홍대점",
            "홍대점",
            "서울특별시",
            "마포구",
            "서교동",
            "I212",
            "비알코올"
        ));

        PlaceSearchCondition condition = new PlaceSearchCondition();
        condition.setSignguNm("강남구");

        Page<PlaceSearchResponse> result = placeMasterRepository.searchPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).signguNm()).isEqualTo("강남구");
    }

    @Test
    @DisplayName("업종 중분류 코드로 장소를 검색한다.")
    void givenIndustryCategory_whenSearchPlaces_thenReturnPlaces() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스 강남점",
            "강남점",
            "서울특별시",
            "강남구",
            "역삼동",
            "I212",
            "비알코올"
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "한식당",
            null,
            "서울특별시",
            "강남구",
            "역삼동",
            "I201",
            "한식"
        ));

        PlaceSearchCondition condition = new PlaceSearchCondition();
        condition.setIndsMclsCd("I212");

        Page<PlaceSearchResponse> result = placeMasterRepository.searchPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).indsMclsCd()).isEqualTo("I212");
    }

    @Test
    @DisplayName("복합 조건으로 장소를 검색한다.")
    void givenMultipleConditions_whenSearchPlaces_thenReturnFilteredPlaces() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스 강남점",
            "강남점",
            "서울특별시",
            "강남구",
            "역삼동",
            "I212",
            "비알코올"
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "스타벅스 홍대점",
            "홍대점",
            "서울특별시",
            "마포구",
            "서교동",
            "I212",
            "비알코올"
        ));

        PlaceSearchCondition condition = new PlaceSearchCondition();
        condition.setKeyword("스타벅스");
        condition.setSignguNm("강남구");
        condition.setIndsMclsCd("I212");

        Page<PlaceSearchResponse> result = placeMasterRepository.searchPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).bizesNm()).isEqualTo("스타벅스 강남점");
    }

    @Test
    @DisplayName("페이징 조건으로 장소 검색 결과를 조회한다.")
    void givenPagingCondition_whenSearchPlaces_thenReturnPagedResult() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스 강남점",
            "강남점",
            "서울특별시",
            "강남구",
            "역삼동",
            "I212",
            "비알코올"
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "메가커피 강남점",
            "강남점",
            "서울특별시",
            "강남구",
            "역삼동",
            "I212",
            "비알코올"
        ));

        PlaceSearchCondition condition = new PlaceSearchCondition();
        condition.setSignguNm("강남구");

        Page<PlaceSearchResponse> result = placeMasterRepository.searchPlaces(
            condition,
            PageRequest.of(0, 1)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("이름순으로 장소를 정렬한다.")
    void givenNameSort_whenSearchPlaces_thenReturnPlacesOrderByName() {
        placeMasterRepository.save(createPlaceMaster(
            "BIZ-001",
            "스타벅스",
            null,
            "서울특별시",
            "강남구",
            "역삼동",
            "I212",
            "비알코올"
        ));

        placeMasterRepository.save(createPlaceMaster(
            "BIZ-002",
            "메가커피",
            null,
            "서울특별시",
            "강남구",
            "역삼동",
            "I212",
            "비알코올"
        ));

        PlaceSearchCondition condition = new PlaceSearchCondition();
        condition.setSignguNm("강남구");
        condition.setSortType(PlaceSortType.NAME);

        Page<PlaceSearchResponse> result = placeMasterRepository.searchPlaces(
            condition,
            PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).bizesNm()).isEqualTo("메가커피");
        assertThat(result.getContent().get(1).bizesNm()).isEqualTo("스타벅스");
    }

    private PlaceMaster createPlaceMaster(
        String bizesId,
        String bizesNm,
        String brchNm,
        String ctprvnNm,
        String signguNm,
        String adongNm,
        String indsMclsCd,
        String indsMclsNm
    ) {
        return PlaceMaster.builder()
            .bizesId(bizesId)
            .bizesNm(bizesNm)
            .brchNm(brchNm)
            .indsLclsCd("I2")
            .indsLclsNm("음식")
            .indsMclsCd(indsMclsCd)
            .indsMclsNm(indsMclsNm)
            .indsSclsCd("I21201")
            .indsSclsNm("카페")
            .ctprvnNm(ctprvnNm)
            .signguNm(signguNm)
            .adongNm(adongNm)
            .ldongNm(adongNm)
            .lnoAdr(ctprvnNm + " " + signguNm + " " + adongNm)
            .rdnmAdr(ctprvnNm + " " + signguNm + " " + adongNm + " 도로명주소")
            .newZipcd("06123")
            .lon(127.0276)
            .lat(37.4979)
            .location("POINT(127.0276 37.4979)")
            .build();
    }
}