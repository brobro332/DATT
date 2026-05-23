package xyz.datt.domain.anchor.dto;

import xyz.datt.domain.anchor.entity.AnchorPlaceCategory;

import java.util.List;

public record AnchorPlaceGroupResponse(
        AnchorPlaceCategory category,
        String categoryName,
        List<AnchorPlaceResponse> places
) {
}