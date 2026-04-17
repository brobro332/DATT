package xyz.datt.domain.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceResponseDto {
    private String name;
    @Setter private String category;
    private String address;
    private Double rating;
    private Integer visitorReviewCount;
    private List<String> imageUrls;
    private String placeUrl;
    private String source;
}