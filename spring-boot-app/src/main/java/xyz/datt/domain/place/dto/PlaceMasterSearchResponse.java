package xyz.datt.domain.place.dto;

import xyz.datt.domain.place.entity.PlaceMaster;

public record PlaceMasterSearchResponse(
    Long id,
    String bizesId,
    String bizesNm,
    String brchNm,
    String indsLclsNm,
    String indsMclsNm,
    String indsSclsNm,
    String ctprvnNm,
    String signguNm,
    String rdnmAdr,
    String lnoAdr,
    Double lon,
    Double lat
) {

    public static PlaceMasterSearchResponse from(PlaceMaster placeMaster) {
        return new PlaceMasterSearchResponse(
            placeMaster.getId(),
            placeMaster.getBizesId(),
            placeMaster.getBizesNm(),
            placeMaster.getBrchNm(),
            placeMaster.getIndsLclsNm(),
            placeMaster.getIndsMclsNm(),
            placeMaster.getIndsSclsNm(),
            placeMaster.getCtprvnNm(),
            placeMaster.getSignguNm(),
            placeMaster.getRdnmAdr(),
            placeMaster.getLnoAdr(),
            placeMaster.getLon(),
            placeMaster.getLat()
        );
    }
}