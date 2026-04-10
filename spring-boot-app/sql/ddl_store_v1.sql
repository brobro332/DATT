CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE store (
                       id BIGSERIAL PRIMARY KEY,

    -- 기본 식별 정보
                       bizes_id VARCHAR(30) NOT NULL,
                       bizes_nm VARCHAR(255) NOT NULL,
                       brch_nm VARCHAR(255) DEFAULT '',

    -- 업종 분류
                       inds_lcls_cd VARCHAR(10),
                       inds_lcls_nm VARCHAR(100),
                       inds_mcls_cd VARCHAR(10),
                       inds_mcls_nm VARCHAR(100),
                       inds_scls_cd VARCHAR(10),
                       inds_scls_nm VARCHAR(100),

    -- 행정구역 정보
                       ctprvn_nm VARCHAR(50),
                       signgu_nm VARCHAR(50),
                       adong_nm VARCHAR(50),
                       ldong_nm VARCHAR(50),

    -- 주소
                       lno_adr VARCHAR(500),
                       rdnm_adr VARCHAR(500),
                       new_zipcd VARCHAR(10),

    -- 위치 정보 (PostGIS)
    -- geometry(Point, 4326): WGS84 좌표계를 사용하는 포인트 타입
                       location GEOMETRY(Point, 4326) NOT NULL,
                       lon DECIMAL(17, 14) NOT NULL,
                       lat DECIMAL(17, 14) NOT NULL,

    -- 메타데이터
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 설정
CREATE UNIQUE INDEX ux_store_bizes_id ON store (bizes_id);
CREATE INDEX idx_store_inds ON store (inds_lcls_cd, inds_mcls_cd);
CREATE INDEX idx_store_region ON store (ctprvn_nm, signgu_nm);

-- GIST 인덱스 (PostgreSQL의 공간 인덱스 방식)
CREATE INDEX sx_store_location ON store USING GIST (location);

-- 주석(Comment) 추가
COMMENT ON COLUMN store.bizes_id IS '상가업소번호';
COMMENT ON COLUMN store.location IS '좌표 정보 (PostGIS Geometry)';