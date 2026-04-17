package xyz.datt.domain.place.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceMasterResponseDto {
    private String bizesId;    // 상가업소번호
    private String bizesNm;    // 상호명
    private String brchNm;     // 지점명
    private String indsLclsCd; // 업종대분류코드
    private String indsLclsNm; // 업종대분류명
    private String indsMclsCd; // 업종중분류코드
    private String indsMclsNm; // 업종중분류명
    private String indsSclsCd; // 업종소분류코드
    private String indsSclsNm; // 업종소분류명
    private String ctprvnNm;   // 시도명
    private String signguNm;   // 시군구명
    private String adongNm;    // 행정동명
    private String ldongNm;    // 법정동명
    private String lnoAdr;     // 지번주소
    private String rdnmAdr;    // 도로명주소
    private String newZipcd;   // 우편번호
    private String lon;        // 경도
    private String lat;        // 위도
}
