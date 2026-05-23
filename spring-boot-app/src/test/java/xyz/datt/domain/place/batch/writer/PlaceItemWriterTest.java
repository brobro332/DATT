package xyz.datt.domain.place.batch.writer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PlaceItemWriterTest {

    @Autowired
    private PlaceMasterRepository placeMasterRepository;

    @Test
    @DisplayName("신규 PlaceMaster를 저장한다.")
    void givenNewPlaceMaster_whenWrite_thenInsertPlaceMaster() throws Exception {
        PlaceItemWriter writer = new PlaceItemWriter(placeMasterRepository);

        PlaceMaster placeMaster = createPlaceMaster(
            "TEST-001",
            "스타벅스 강남점",
            "서울특별시 강남구 테헤란로 123"
        );

        writer.write(Chunk.of(placeMaster));

        PlaceMaster savedPlaceMaster = placeMasterRepository.findByBizesId("TEST-001")
            .orElseThrow();

        assertThat(savedPlaceMaster.getBizesNm()).isEqualTo("스타벅스 강남점");
        assertThat(savedPlaceMaster.getRdnmAdr()).isEqualTo("서울특별시 강남구 테헤란로 123");
    }

    @Test
    @DisplayName("이미 존재하는 PlaceMaster는 수정한다.")
    void givenExistingPlaceMaster_whenWrite_thenUpdatePlaceMaster() throws Exception {
        PlaceMaster existingPlaceMaster = createPlaceMaster(
            "TEST-001",
            "스타벅스 강남점",
            "서울특별시 강남구 테헤란로 123"
        );

        placeMasterRepository.save(existingPlaceMaster);

        PlaceItemWriter writer = new PlaceItemWriter(placeMasterRepository);

        PlaceMaster updatedPlaceMaster = createPlaceMaster(
            "TEST-001",
            "스타벅스 강남역점",
            "서울특별시 강남구 강남대로 456"
        );

        writer.write(Chunk.of(updatedPlaceMaster));

        PlaceMaster result = placeMasterRepository.findByBizesId("TEST-001")
                .orElseThrow();

        assertThat(result.getBizesNm()).isEqualTo("스타벅스 강남역점");
        assertThat(result.getRdnmAdr()).isEqualTo("서울특별시 강남구 강남대로 456");
    }

    private PlaceMaster createPlaceMaster(
        String bizesId,
        String bizesNm,
        String rdnmAdr
    ) {
        return PlaceMaster.builder()
            .bizesId(bizesId)
            .bizesNm(bizesNm)
            .brchNm(null)
            .indsLclsCd("I2")
            .indsLclsNm("음식")
            .indsMclsCd("I212")
            .indsMclsNm("비알코올")
            .indsSclsCd("I21201")
            .indsSclsNm("카페")
            .ctprvnNm("서울특별시")
            .signguNm("강남구")
            .adongNm("역삼동")
            .ldongNm("역삼동")
            .lnoAdr("서울특별시 강남구 역삼동 123-1")
            .rdnmAdr(rdnmAdr)
            .newZipcd("06123")
            .lon(127.0276)
            .lat(37.4979)
            .location("POINT(127.0276 37.4979)")
            .build();
    }
}