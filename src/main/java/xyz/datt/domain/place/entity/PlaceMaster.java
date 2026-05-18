package xyz.datt.domain.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.datt.global.entity.BaseEntity;

@Getter
@Entity
@Table(
    name = "place_master",
    indexes = {
        @Index(name = "idx_place_master_bizes_id", columnList = "bizes_id"),
        @Index(name = "idx_place_master_bizes_nm", columnList = "bizes_nm"),
        @Index(name = "idx_place_master_region", columnList = "ctprvn_nm, signgu_nm"),
        @Index(name = "idx_place_master_category", columnList = "inds_lcls_nm")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceMaster extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String bizesId; // 상가업소번호

    @Column(nullable = false, length = 200)
    private String bizesNm; // 상호명

    @Column(length = 200)
    private String brchNm; // 지점명

    @Column(length = 20)
    private String indsLclsCd; // 업종 대분류 코드

    @Column(length = 100)
    private String indsLclsNm; // 업종 대분류명

    @Column(length = 20)
    private String indsMclsCd; // 업종 중분류 코드

    @Column(length = 100)
    private String indsMclsNm; // 업종 중분류명

    @Column(length = 20)
    private String indsSclsCd; // 업종 소분류 코드

    @Column(length = 100)
    private String indsSclsNm; // 업종 소분류명

    @Column(length = 100)
    private String ctprvnNm; // 시도명

    @Column(length = 100)
    private String signguNm; // 시군구명

    @Column(length = 100)
    private String adongNm; // 행정동명

    @Column(length = 100)
    private String ldongNm; // 법정동명

    @Column(length = 500)
    private String lnoAdr; // 지번주소

    @Column(length = 500)
    private String rdnmAdr; // 도로명주소

    @Column(length = 20)
    private String newZipcd; // 우편번호

    @Column
    private Double lon; // 경도

    @Column
    private Double lat; // 위도

    @Column(length = 100)
    private String location; // 좌표 문자열

    @Builder
    private PlaceMaster(
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
        String location
    ) {
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
        this.lon = lon;
        this.lat = lat;
        this.location = location;
    }
}