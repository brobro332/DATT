package xyz.datt.domain.place.mapper;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import xyz.datt.domain.place.dto.PlaceMasterResponseDto;
import xyz.datt.domain.place.entity.PlaceMaster;

import java.math.BigDecimal;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @org.mapstruct.Builder(disableBuilder = true))
public abstract class PlaceMasterMapper {
    protected final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "location", source = "dto", qualifiedByName = "toLocation")
    @Mapping(target = "lon", source = "lon", qualifiedByName = "toBigDecimal")
    @Mapping(target = "lat", source = "lat", qualifiedByName = "toBigDecimal")
    public abstract PlaceMaster toEntity(PlaceMasterResponseDto dto);

    @Named("toLocation")
    protected Point toLocation(PlaceMasterResponseDto dto) {
        if (dto.getLon() == null || dto.getLat() == null) return null;
        return geometryFactory.createPoint(new Coordinate(
            Double.parseDouble(dto.getLon()),
            Double.parseDouble(dto.getLat())
        ));
    }

    @Named("toBigDecimal")
    protected BigDecimal toBigDecimal(String value) {
        return (value != null) ? new BigDecimal(value) : null;
    }
}
