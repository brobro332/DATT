package xyz.datt.domain.place.dto;

import xyz.datt.domain.place.entity.PlaceMaster;

public record PlaceSearchResponse(
    Long id,
    String bizesNm,
    String brchNm,
    String indsMclsCd,
    String indsMclsNm,
    String ctprvnNm,
    String signguNm,
    String adongNm,
    String rdnmAdr,
    Double lon,
    Double lat
) {

    public static PlaceSearchResponse from(PlaceMaster placeMaster) {
        return new PlaceSearchResponse(
            placeMaster.getId(),
            placeMaster.getBizesNm(),
            placeMaster.getBrchNm(),
            placeMaster.getIndsMclsCd(),
            placeMaster.getIndsMclsNm(),
            placeMaster.getCtprvnNm(),
            placeMaster.getSignguNm(),
            placeMaster.getAdongNm(),
            placeMaster.getRdnmAdr(),
            placeMaster.getLon(),
            placeMaster.getLat()
        );
    }
}