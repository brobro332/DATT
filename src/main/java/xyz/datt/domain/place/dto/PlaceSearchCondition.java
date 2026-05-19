package xyz.datt.domain.place.dto;

import lombok.Getter;
import lombok.Setter;
import xyz.datt.domain.place.entity.PlaceSortType;

@Getter
@Setter
public class PlaceSearchCondition {
    private String keyword;
    private String ctprvnNm;
    private String signguNm;
    private String adongNm;
    private String indsMclsCd;
    private PlaceSortType sortType = PlaceSortType.LATEST;
}