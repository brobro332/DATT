package xyz.datt.domain.place.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceNearbyCondition {
    private Double lon;
    private Double lat;
    private Double radiusKm = 3.0;
    private String keyword;
    private String indsMclsCd;
}