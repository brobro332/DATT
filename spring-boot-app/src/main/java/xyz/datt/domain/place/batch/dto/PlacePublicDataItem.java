package xyz.datt.domain.place.batch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlacePublicDataItem {
    private String bizesId;
    private String bizesNm;
    private String brchNm;

    private String indsLclsCd;
    private String indsLclsNm;
    private String indsMclsCd;
    private String indsMclsNm;
    private String indsSclsCd;
    private String indsSclsNm;

    private String ctprvnNm;
    private String signguNm;
    private String adongNm;
    private String ldongNm;

    private String lnoAdr;
    private String rdnmAdr;
    private String newZipcd;

    private Double lon;
    private Double lat;
}