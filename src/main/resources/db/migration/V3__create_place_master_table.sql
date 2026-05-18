CREATE TABLE place_master (
      id BIGSERIAL PRIMARY KEY,

      bizes_id VARCHAR(50) NOT NULL UNIQUE,
      bizes_nm VARCHAR(200) NOT NULL,
      brch_nm VARCHAR(200),

      inds_lcls_cd VARCHAR(20),
      inds_lcls_nm VARCHAR(100),
      inds_mcls_cd VARCHAR(20),
      inds_mcls_nm VARCHAR(100),
      inds_scls_cd VARCHAR(20),
      inds_scls_nm VARCHAR(100),

      ctprvn_nm VARCHAR(100),
      signgu_nm VARCHAR(100),
      adong_nm VARCHAR(100),
      ldong_nm VARCHAR(100),

      lno_adr VARCHAR(500),
      rdnm_adr VARCHAR(500),
      new_zipcd VARCHAR(20),

      lon DOUBLE PRECISION,
      lat DOUBLE PRECISION,
      location VARCHAR(100),

      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_place_master_bizes_nm
    ON place_master (bizes_nm);

CREATE INDEX idx_place_master_region
    ON place_master (ctprvn_nm, signgu_nm);

CREATE INDEX idx_place_master_category
    ON place_master (inds_lcls_nm);

COMMENT ON TABLE place_master IS '공공데이터 기반 장소 마스터 테이블';

COMMENT ON COLUMN place_master.id IS '장소 마스터 PK';
COMMENT ON COLUMN place_master.bizes_id IS '상가업소번호';
COMMENT ON COLUMN place_master.bizes_nm IS '상호명';
COMMENT ON COLUMN place_master.brch_nm IS '지점명';

COMMENT ON COLUMN place_master.inds_lcls_cd IS '업종 대분류 코드';
COMMENT ON COLUMN place_master.inds_lcls_nm IS '업종 대분류명';
COMMENT ON COLUMN place_master.inds_mcls_cd IS '업종 중분류 코드';
COMMENT ON COLUMN place_master.inds_mcls_nm IS '업종 중분류명';
COMMENT ON COLUMN place_master.inds_scls_cd IS '업종 소분류 코드';
COMMENT ON COLUMN place_master.inds_scls_nm IS '업종 소분류명';

COMMENT ON COLUMN place_master.ctprvn_nm IS '시도명';
COMMENT ON COLUMN place_master.signgu_nm IS '시군구명';
COMMENT ON COLUMN place_master.adong_nm IS '행정동명';
COMMENT ON COLUMN place_master.ldong_nm IS '법정동명';

COMMENT ON COLUMN place_master.lno_adr IS '지번주소';
COMMENT ON COLUMN place_master.rdnm_adr IS '도로명주소';
COMMENT ON COLUMN place_master.new_zipcd IS '신우편번호';

COMMENT ON COLUMN place_master.lon IS '경도';
COMMENT ON COLUMN place_master.lat IS '위도';
COMMENT ON COLUMN place_master.location IS '좌표 문자열';

COMMENT ON COLUMN place_master.created_at IS '생성일시';
COMMENT ON COLUMN place_master.updated_at IS '수정일시';