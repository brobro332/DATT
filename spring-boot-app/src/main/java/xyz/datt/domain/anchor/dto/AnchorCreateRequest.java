package xyz.datt.domain.anchor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnchorCreateRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        Long basePlaceId,

        @Size(max = 100)
        String basePlaceName,

        @Size(max = 255)
        String baseAddress,

        Double baseLon,

        Double baseLat,

        Double radiusKm,

        Boolean isPublic
) {
}