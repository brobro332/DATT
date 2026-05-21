package xyz.datt.domain.place.dto;

import xyz.datt.domain.place.entity.PlaceMaster;

import java.time.LocalDateTime;

public record PlaceDetailResponse(
    Long id,
    String bizesId,

    String bizesNm,
    String brchNm,

    String indsLclsCd,
    String indsLclsNm,
    String indsMclsCd,
    String indsMclsNm,
    String indsSclsCd,
    String indsSclsNm,

    String ctprvnNm,
    String signguNm,
    String adongNm,
    String ldongNm,

    String lnoAdr,
    String rdnmAdr,
    String newZipcd,

    Double lon,
    Double lat,

    Boolean isBookmarked,

    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PlaceDetailResponse from(
        PlaceMaster placeMaster,
        boolean isBookmarked
    ) {
        return new PlaceDetailResponse(
            placeMaster.getId(),
            placeMaster.getBizesId(),

            placeMaster.getBizesNm(),
            placeMaster.getBrchNm(),

            placeMaster.getIndsLclsCd(),
            placeMaster.getIndsLclsNm(),
            placeMaster.getIndsMclsCd(),
            placeMaster.getIndsMclsNm(),
            placeMaster.getIndsSclsCd(),
            placeMaster.getIndsSclsNm(),

            placeMaster.getCtprvnNm(),
            placeMaster.getSignguNm(),
            placeMaster.getAdongNm(),
            placeMaster.getLdongNm(),

            placeMaster.getLnoAdr(),
            placeMaster.getRdnmAdr(),
            placeMaster.getNewZipcd(),

            placeMaster.getLon(),
            placeMaster.getLat(),

            isBookmarked,

            placeMaster.getCreatedAt(),
            placeMaster.getUpdatedAt()
        );
    }
}