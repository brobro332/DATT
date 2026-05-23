INSERT INTO place_master (
    bizes_id,
    bizes_nm,
    brch_nm,

    inds_lcls_cd,
    inds_lcls_nm,

    inds_mcls_cd,
    inds_mcls_nm,

    inds_scls_cd,
    inds_scls_nm,

    ctprvn_nm,
    signgu_nm,
    adong_nm,
    ldong_nm,

    lno_adr,
    rdnm_adr,
    new_zipcd,

    lon,
    lat,
    location,

    created_at,
    updated_at
) VALUES (
    'TEST-001',
    '스타벅스 강남점',
    NULL,

    'I2',
    '음식',

    'I201',
    '카페',

    'I20101',
    '커피전문점/카페',

    '서울특별시',
    '강남구',
    '역삼동',
    '역삼동',

    '서울특별시 강남구 역삼동 123-1',
    '서울특별시 강남구 테헤란로 123',
    '06123',

    127.0276,
    37.4979,
    'POINT(127.0276 37.4979)',

    NOW(),
    NOW()
);

INSERT INTO place_master (
    bizes_id,
    bizes_nm,
    brch_nm,

    inds_lcls_cd,
    inds_lcls_nm,

    inds_mcls_cd,
    inds_mcls_nm,

    inds_scls_cd,
    inds_scls_nm,

    ctprvn_nm,
    signgu_nm,
    adong_nm,
    ldong_nm,

    lno_adr,
    rdnm_adr,
    new_zipcd,

    lon,
    lat,
    location,

    created_at,
    updated_at
) VALUES (
    'TEST-002',
    '메가커피 선릉점',
    NULL,

    'I2',
    '음식',

    'I201',
    '카페',

    'I20101',
    '커피전문점/카페',

    '서울특별시',
    '강남구',
    '대치동',
    '대치동',

    '서울특별시 강남구 대치동 456-7',
    '서울특별시 강남구 선릉로 456',
    '06124',

    127.0489,
    37.5045,
    'POINT(127.0489 37.5045)',

    NOW(),
    NOW()
);