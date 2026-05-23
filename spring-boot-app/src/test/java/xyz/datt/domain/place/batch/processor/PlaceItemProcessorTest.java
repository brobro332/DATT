package xyz.datt.domain.place.batch.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.datt.domain.place.batch.dto.PlacePublicDataItem;
import xyz.datt.domain.place.entity.PlaceMaster;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceItemProcessorTest {
    private final PlaceItemProcessor placeItemProcessor = new PlaceItemProcessor();

    @Test
    @DisplayName("공공데이터 Item을 PlaceMaster로 변환한다.")
    void givenPublicDataItem_whenProcess_thenReturnPlaceMaster() throws Exception {
        PlacePublicDataItem item = createItem();

        PlaceMaster result = placeItemProcessor.process(item);

        assertThat(result).isNotNull();
        assertThat(result.getBizesId()).isEqualTo("TEST-001");
        assertThat(result.getBizesNm()).isEqualTo("스타벅스 강남점");
        assertThat(result.getIndsMclsCd()).isEqualTo("I212");
        assertThat(result.getIndsMclsNm()).isEqualTo("비알코올");
        assertThat(result.getLocation()).isEqualTo("POINT(127.0276 37.4979)");
    }

    @Test
    @DisplayName("상가업소번호가 없으면 필터링한다.")
    void givenBlankBizesId_whenProcess_thenReturnNull() throws Exception {
        PlacePublicDataItem item = createItem();
        item.setBizesId(" ");

        PlaceMaster result = placeItemProcessor.process(item);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("상호명이 없으면 필터링한다.")
    void givenBlankBizesNm_whenProcess_thenReturnNull() throws Exception {
        PlacePublicDataItem item = createItem();
        item.setBizesNm(" ");

        PlaceMaster result = placeItemProcessor.process(item);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("좌표가 없으면 location은 null이다.")
    void givenNullCoordinate_whenProcess_thenLocationIsNull() throws Exception {
        PlacePublicDataItem item = createItem();
        item.setLon(null);
        item.setLat(null);

        PlaceMaster result = placeItemProcessor.process(item);

        assertThat(result).isNotNull();
        assertThat(result.getLocation()).isNull();
    }

    private PlacePublicDataItem createItem() {
        PlacePublicDataItem item = new PlacePublicDataItem();

        item.setBizesId("TEST-001");
        item.setBizesNm("스타벅스 강남점");
        item.setBrchNm(null);

        item.setIndsLclsCd("I2");
        item.setIndsLclsNm("음식");
        item.setIndsMclsCd("I212");
        item.setIndsMclsNm("비알코올");
        item.setIndsSclsCd("I21201");
        item.setIndsSclsNm("카페");

        item.setCtprvnNm("서울특별시");
        item.setSignguNm("강남구");
        item.setAdongNm("역삼동");
        item.setLdongNm("역삼동");

        item.setLnoAdr("서울특별시 강남구 역삼동 123-1");
        item.setRdnmAdr("서울특별시 강남구 테헤란로 123");
        item.setNewZipcd("06123");

        item.setLon(127.0276);
        item.setLat(37.4979);

        return item;
    }
}