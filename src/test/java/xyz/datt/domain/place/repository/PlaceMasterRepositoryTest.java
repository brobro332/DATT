package xyz.datt.domain.place.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.place.entity.PlaceMaster;

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
}