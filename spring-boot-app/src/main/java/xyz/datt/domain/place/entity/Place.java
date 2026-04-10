package xyz.datt.domain.place.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "place", indexes = {
    @Index(name = "ux_place_bizes_id", columnList = "bizes_id", unique = true),
    @Index(name = "idx_place_inds", columnList = "inds_lcls_cd, inds_mcls_cd"),
    @Index(name = "idx_place_region", columnList = "ctprvn_nm, signgu_nm")
})
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bizes_id", length = 30, nullable = false)
    private String bizesId; // 상가업소번호

    @Column(name = "bizes_nm", nullable = false)
    private String bizesNm; // 상호명

    @Column(name = "brch_nm")
    private String brchNm; // 지점명

    // 업종 분류
    private String indsLclsCd;
    private String indsLclsNm;
    private String indsMclsCd;
    private String indsMclsNm;
    private String indsSclsCd;
    private String indsSclsNm;

    // 행정구역 정보
    private String ctprvnNm; // 시도명
    private String signguNm; // 시군구명
    private String adongNm;  // 행정동명
    private String ldongNm;  // 법정동명

    // 주소
    @Column(length = 500)
    private String lnoAdr;   // 지번주소
    @Column(length = 500)
    private String rdnmAdr;  // 도로명주소
    private String newZipcd; // 우편번호

    // 위치 정보
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;

    private BigDecimal lon; // 원본 좌표 보관용
    private BigDecimal lat;

    @Builder
    public Place(String bizesId, String bizesNm, String brchNm, String indsLclsCd, String indsLclsNm,
                 String indsMclsCd, String indsMclsNm, String indsSclsCd, String indsSclsNm,
                 String ctprvnNm, String signguNm, String adongNm, String ldongNm,
                 String lnoAdr, String rdnmAdr, String newZipcd, Point location, BigDecimal lon, BigDecimal lat) {
        this.bizesId = bizesId;
        this.bizesNm = bizesNm;
        this.brchNm = brchNm;
        this.indsLclsCd = indsLclsCd;
        this.indsLclsNm = indsLclsNm;
        this.indsMclsCd = indsMclsCd;
        this.indsMclsNm = indsMclsNm;
        this.indsSclsCd = indsSclsCd;
        this.indsSclsNm = indsSclsNm;
        this.ctprvnNm = ctprvnNm;
        this.signguNm = signguNm;
        this.adongNm = adongNm;
        this.ldongNm = ldongNm;
        this.lnoAdr = lnoAdr;
        this.rdnmAdr = rdnmAdr;
        this.newZipcd = newZipcd;
        this.location = location;
        this.lon = lon;
        this.lat = lat;
    }
}