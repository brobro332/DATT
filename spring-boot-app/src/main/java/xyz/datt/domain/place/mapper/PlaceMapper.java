package xyz.datt.domain.place.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import xyz.datt.domain.place.dto.PlaceResponseDto;
import xyz.datt.domain.place.entity.Place;
import xyz.datt.domain.place.entity.Platform;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @org.mapstruct.Builder(disableBuilder = true),
        imports = { Platform.class })
public interface PlaceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keyword", source = "keyword")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "platform", source = "platform")
    @Mapping(target = "reviewCount", source = "dto.visitorReviewCount")
    @Mapping(target = "imageUrls", expression = "java(joinImages(dto.getImageUrls()))")
    @Mapping(target = "placeUrl", source = "dto.placeUrl")
    @Mapping(target = "rating", source = "dto.rating")
    Place toEntity(PlaceResponseDto dto, String keyword, String category, Platform platform);

    @Mapping(target = "imageUrls", expression = "java(splitImages(entity.getImageUrls()))")
    @Mapping(target = "visitorReviewCount", source = "reviewCount")
    @Mapping(target = "source", expression = "java(entity.getPlatform().getCode())")
    @Mapping(target = "rating", source = "entity.rating")
    PlaceResponseDto toResponseDto(Place entity);

    default String joinImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) return null;
        return String.join("|", imageUrls);
    }

    default List<String> splitImages(String imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) return java.util.Collections.emptyList();
        return java.util.Arrays.asList(imageUrls.split("\\|"));
    }
}