package xyz.datt.domain.place.batch.processor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import xyz.datt.domain.place.dto.PlaceMasterResponseDto;
import xyz.datt.domain.place.entity.PlaceMaster;

import java.math.BigDecimal;

@Component
public class PlaceItemProcessor implements ItemProcessor<PlaceMasterResponseDto, PlaceMaster> {
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public PlaceMaster process(PlaceMasterResponseDto dto) {
        BigDecimal lon = new BigDecimal(dto.getLon());
        BigDecimal lat = new BigDecimal(dto.getLat());
        Point location = geometryFactory.createPoint(new Coordinate(lon.doubleValue(), lat.doubleValue()));

        return PlaceMaster.builder()
            .bizesId(dto.getBizesId())
            .bizesNm(dto.getBizesNm())
            .brchNm(dto.getBrchNm())
            .indsLclsCd(dto.getIndsLclsCd())
            .indsLclsNm(dto.getIndsLclsNm())
            .indsMclsCd(dto.getIndsMclsCd())
            .indsMclsNm(dto.getIndsMclsNm())
            .indsSclsCd(dto.getIndsSclsCd())
            .indsSclsNm(dto.getIndsSclsNm())
            .ctprvnNm(dto.getCtprvnNm())
            .signguNm(dto.getSignguNm())
            .adongNm(dto.getAdongNm())
            .ldongNm(dto.getLdongNm())
            .lnoAdr(dto.getLnoAdr())
            .rdnmAdr(dto.getRdnmAdr())
            .newZipcd(dto.getNewZipcd())
            .location(location)
            .lon(lon)
            .lat(lat)
            .build();
    }
}
