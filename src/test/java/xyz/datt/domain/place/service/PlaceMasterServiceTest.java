package xyz.datt.domain.place.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.place.dto.PlaceMasterSearchResponse;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PlaceMasterServiceTest {
    @Autowired
    private PlaceMasterService placeMasterService;

    @Autowired
    private PlaceMasterRepository placeMasterRepository;

    @Test
    @DisplayName("키워드로 장소 검색에 성공한다.")
    void givenKeyword_whenSearchByKeyword_thenReturnPlaceMasters() {
        placeMasterRepository.save(createPlaceMaster("TEST-001", "스타벅스 강남점"));
        placeMasterRepository.save(createPlaceMaster("TEST-002", "메가커피 선릉점"));

        List<PlaceMasterSearchResponse> result =
            placeMasterService.searchByKeyword("스타벅스");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).bizesNm())
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