package xyz.datt.domain.anchor.dto;

import xyz.datt.domain.place.dto.PlaceResponseDto;

import java.util.List;
import java.util.Map;

public record AnchorResponseDto(String anchorId,
                                String keyword,
                                String shareUrl,
                                Map<String, Map<String, List<PlaceResponseDto>>> content) { }
