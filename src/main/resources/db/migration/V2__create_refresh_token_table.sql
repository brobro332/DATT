CREATE TABLE refresh_token (
   id BIGSERIAL PRIMARY KEY,
   member_id BIGINT NOT NULL UNIQUE,
   token VARCHAR(500) NOT NULL,
   expired_at TIMESTAMP NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE refresh_token IS 'Refresh Token 테이블';
COMMENT ON COLUMN refresh_token.id IS 'Refresh Token PK';
COMMENT ON COLUMN refresh_token.member_id IS '회원 PK';
COMMENT ON COLUMN refresh_token.token IS 'Refresh Token 값';
COMMENT ON COLUMN refresh_token.expired_at IS 'Refresh Token 만료일시';
COMMENT ON COLUMN refresh_token.created_at IS '생성일시';
COMMENT ON COLUMN refresh_token.updated_at IS '수정일시';