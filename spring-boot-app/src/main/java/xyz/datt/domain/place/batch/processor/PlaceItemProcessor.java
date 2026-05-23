package xyz.datt.domain.place.batch.processor;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import xyz.datt.domain.place.batch.dto.PlacePublicDataItem;
import xyz.datt.domain.place.entity.PlaceMaster;

public class PlaceItemProcessor implements ItemProcessor<PlacePublicDataItem, PlaceMaster> {
    @Override
    public PlaceMaster process(PlacePublicDataItem item) {
        if (item == null || isBlank(item.getBizesId()) || isBlank(item.getBizesNm())) {
            return null;
        }

        return PlaceMaster.builder()
            .bizesId(trim(item.getBizesId()))
            .bizesNm(trim(item.getBizesNm()))
            .brchNm(trim(item.getBrchNm()))
            .indsLclsCd(trim(item.getIndsLclsCd()))
            .indsLclsNm(trim(item.getIndsLclsNm()))
            .indsMclsCd(trim(item.getIndsMclsCd()))
            .indsMclsNm(trim(item.getIndsMclsNm()))
            .indsSclsCd(trim(item.getIndsSclsCd()))
            .indsSclsNm(trim(item.getIndsSclsNm()))
            .ctprvnNm(trim(item.getCtprvnNm()))
            .signguNm(trim(item.getSignguNm()))
            .adongNm(trim(item.getAdongNm()))
            .ldongNm(trim(item.getLdongNm()))
            .lnoAdr(trim(item.getLnoAdr()))
            .rdnmAdr(trim(item.getRdnmAdr()))
            .newZipcd(trim(item.getNewZipcd()))
            .lon(item.getLon())
            .lat(item.getLat())
            .location(createLocation(item.getLon(), item.getLat()))
            .build();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String createLocation(Double lon, Double lat) {
        if (lon == null || lat == null) {
            return null;
        }

        return "POINT(" + lon + " " + lat + ")";
    }
}