CREATE TABLE member (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(30) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    level INT NOT NULL DEFAULT 1,
    exp INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_member_role CHECK (role IN ('USER', 'ADMIN')),
    CONSTRAINT chk_member_level CHECK (level >= 1),
    CONSTRAINT chk_member_exp CHECK (exp >= 0)
);

COMMENT ON TABLE member IS '회원 테이블';
COMMENT ON COLUMN member.id IS '회원 PK';
COMMENT ON COLUMN member.email IS '이메일';
COMMENT ON COLUMN member.password IS '암호화된 비밀번호';
COMMENT ON COLUMN member.nickname IS '닉네임';
COMMENT ON COLUMN member.role IS '회원 권한';
COMMENT ON COLUMN member.level IS '회원 레벨';
COMMENT ON COLUMN member.exp IS '회원 경험치';
COMMENT ON COLUMN member.created_at IS '생성일시';
COMMENT ON COLUMN member.updated_at IS '수정일시';