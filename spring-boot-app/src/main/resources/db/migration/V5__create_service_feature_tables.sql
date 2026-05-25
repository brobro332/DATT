CREATE TABLE place_bookmarks (
                                 id BIGSERIAL PRIMARY KEY,
                                 member_id BIGINT NOT NULL,
                                 place_id BIGINT NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT uk_place_bookmark_member_place UNIQUE (member_id, place_id),
                                 CONSTRAINT fk_place_bookmarks_member FOREIGN KEY (member_id) REFERENCES member (id),
                                 CONSTRAINT fk_place_bookmarks_place FOREIGN KEY (place_id) REFERENCES place_master (id)
);

COMMENT ON TABLE place_bookmarks IS '장소 북마크 테이블';
COMMENT ON COLUMN place_bookmarks.id IS '장소 북마크 PK';
COMMENT ON COLUMN place_bookmarks.member_id IS '회원 PK';
COMMENT ON COLUMN place_bookmarks.place_id IS '장소 PK';
COMMENT ON COLUMN place_bookmarks.created_at IS '생성일시';
COMMENT ON COLUMN place_bookmarks.updated_at IS '수정일시';


CREATE TABLE place_review (
                              id BIGSERIAL PRIMARY KEY,
                              member_id BIGINT NOT NULL,
                              place_id BIGINT NOT NULL,
                              rating INT NOT NULL,
                              content VARCHAR(1000) NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT uk_place_review_member_place UNIQUE (member_id, place_id),
                              CONSTRAINT fk_place_review_member FOREIGN KEY (member_id) REFERENCES member (id),
                              CONSTRAINT fk_place_review_place FOREIGN KEY (place_id) REFERENCES place_master (id),
                              CONSTRAINT chk_place_review_rating CHECK (rating BETWEEN 1 AND 5)
);

COMMENT ON TABLE place_review IS '장소 리뷰 및 평점 테이블';
COMMENT ON COLUMN place_review.id IS '장소 리뷰 PK';
COMMENT ON COLUMN place_review.member_id IS '회원 PK';
COMMENT ON COLUMN place_review.place_id IS '장소 PK';
COMMENT ON COLUMN place_review.rating IS '평점';
COMMENT ON COLUMN place_review.content IS '리뷰 내용';
COMMENT ON COLUMN place_review.created_at IS '생성일시';
COMMENT ON COLUMN place_review.updated_at IS '수정일시';


CREATE TABLE anchor (
                        id BIGSERIAL PRIMARY KEY,
                        member_id BIGINT NOT NULL,
                        title VARCHAR(100) NOT NULL,
                        base_place_id BIGINT,
                        base_place_name VARCHAR(100),
                        base_address VARCHAR(255),
                        base_lon DOUBLE PRECISION NOT NULL,
                        base_lat DOUBLE PRECISION NOT NULL,
                        radius_km DOUBLE PRECISION NOT NULL,
                        is_public BOOLEAN NOT NULL,
                        view_count BIGINT NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_anchor_member FOREIGN KEY (member_id) REFERENCES member (id),
                        CONSTRAINT fk_anchor_base_place FOREIGN KEY (base_place_id) REFERENCES place_master (id)
);

COMMENT ON TABLE anchor IS '지역 기반 경험 큐레이션 Anchor 테이블';
COMMENT ON COLUMN anchor.id IS 'Anchor PK';
COMMENT ON COLUMN anchor.member_id IS '생성 회원 PK';
COMMENT ON COLUMN anchor.title IS 'Anchor 제목';
COMMENT ON COLUMN anchor.base_place_id IS '기준 장소 PK';
COMMENT ON COLUMN anchor.base_place_name IS '기준 장소명';
COMMENT ON COLUMN anchor.base_address IS '기준 주소';
COMMENT ON COLUMN anchor.base_lon IS '기준 경도';
COMMENT ON COLUMN anchor.base_lat IS '기준 위도';
COMMENT ON COLUMN anchor.radius_km IS '추천 반경 km';
COMMENT ON COLUMN anchor.is_public IS '공개 여부';
COMMENT ON COLUMN anchor.view_count IS '조회수';
COMMENT ON COLUMN anchor.created_at IS '생성일시';
COMMENT ON COLUMN anchor.updated_at IS '수정일시';


CREATE TABLE anchor_place (
                              id BIGSERIAL PRIMARY KEY,
                              anchor_id BIGINT NOT NULL,
                              place_id BIGINT NOT NULL,
                              category VARCHAR(30) NOT NULL,
                              distance_km DOUBLE PRECISION NOT NULL,
                              recommend_order INT NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_anchor_place_anchor FOREIGN KEY (anchor_id) REFERENCES anchor (id),
                              CONSTRAINT fk_anchor_place_place FOREIGN KEY (place_id) REFERENCES place_master (id),
                              CONSTRAINT chk_anchor_place_category CHECK (category IN ('FOOD', 'CAFE', 'BAR', 'STAY', 'PLAY'))
);

COMMENT ON TABLE anchor_place IS 'Anchor 추천 장소 테이블';
COMMENT ON COLUMN anchor_place.id IS 'Anchor 추천 장소 PK';
COMMENT ON COLUMN anchor_place.anchor_id IS 'Anchor PK';
COMMENT ON COLUMN anchor_place.place_id IS '장소 PK';
COMMENT ON COLUMN anchor_place.category IS '추천 카테고리';
COMMENT ON COLUMN anchor_place.distance_km IS '기준 위치로부터 거리 km';
COMMENT ON COLUMN anchor_place.recommend_order IS '추천 순서';
COMMENT ON COLUMN anchor_place.created_at IS '생성일시';
COMMENT ON COLUMN anchor_place.updated_at IS '수정일시';


CREATE TABLE anchor_like (
                             id BIGSERIAL PRIMARY KEY,
                             member_id BIGINT NOT NULL,
                             anchor_id BIGINT NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT uk_anchor_like_member_anchor UNIQUE (member_id, anchor_id),
                             CONSTRAINT fk_anchor_like_member FOREIGN KEY (member_id) REFERENCES member (id),
                             CONSTRAINT fk_anchor_like_anchor FOREIGN KEY (anchor_id) REFERENCES anchor (id)
);

COMMENT ON TABLE anchor_like IS 'Anchor 좋아요 테이블';
COMMENT ON COLUMN anchor_like.id IS 'Anchor 좋아요 PK';
COMMENT ON COLUMN anchor_like.member_id IS '회원 PK';
COMMENT ON COLUMN anchor_like.anchor_id IS 'Anchor PK';
COMMENT ON COLUMN anchor_like.created_at IS '생성일시';
COMMENT ON COLUMN anchor_like.updated_at IS '수정일시';


CREATE TABLE achievement (
                             id BIGSERIAL PRIMARY KEY,
                             code VARCHAR(50) NOT NULL UNIQUE,
                             name VARCHAR(100) NOT NULL,
                             description VARCHAR(255) NOT NULL,
                             reward_exp INT NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE achievement IS '업적 메타 정보';
COMMENT ON COLUMN achievement.id IS '업적 PK';
COMMENT ON COLUMN achievement.code IS '업적 코드';
COMMENT ON COLUMN achievement.name IS '업적 이름';
COMMENT ON COLUMN achievement.description IS '업적 설명';
COMMENT ON COLUMN achievement.reward_exp IS '업적 달성 보상 경험치';
COMMENT ON COLUMN achievement.created_at IS '생성일시';
COMMENT ON COLUMN achievement.updated_at IS '수정일시';


CREATE TABLE member_achievement (
                                    id BIGSERIAL PRIMARY KEY,
                                    member_id BIGINT NOT NULL,
                                    achievement_id BIGINT NOT NULL,
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT uk_member_achievement_member_achievement UNIQUE (member_id, achievement_id),
                                    CONSTRAINT fk_member_achievement_member FOREIGN KEY (member_id) REFERENCES member (id),
                                    CONSTRAINT fk_member_achievement_achievement FOREIGN KEY (achievement_id) REFERENCES achievement (id)
);

COMMENT ON TABLE member_achievement IS '사용자 업적 획득 정보';
COMMENT ON COLUMN member_achievement.id IS '사용자 업적 PK';
COMMENT ON COLUMN member_achievement.member_id IS '회원 PK';
COMMENT ON COLUMN member_achievement.achievement_id IS '업적 PK';
COMMENT ON COLUMN member_achievement.created_at IS '생성일시';
COMMENT ON COLUMN member_achievement.updated_at IS '수정일시';


CREATE TABLE title (
                       id BIGSERIAL PRIMARY KEY,
                       code VARCHAR(50) NOT NULL UNIQUE,
                       name VARCHAR(100) NOT NULL,
                       description VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE title IS '칭호 메타 정보';
COMMENT ON COLUMN title.id IS '칭호 PK';
COMMENT ON COLUMN title.code IS '칭호 코드';
COMMENT ON COLUMN title.name IS '칭호 이름';
COMMENT ON COLUMN title.description IS '칭호 설명';
COMMENT ON COLUMN title.created_at IS '생성일시';
COMMENT ON COLUMN title.updated_at IS '수정일시';


CREATE TABLE member_title (
                              id BIGSERIAL PRIMARY KEY,
                              member_id BIGINT NOT NULL,
                              title_id BIGINT NOT NULL,
                              selected BOOLEAN NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT uk_member_title_member_title UNIQUE (member_id, title_id),
                              CONSTRAINT fk_member_title_member FOREIGN KEY (member_id) REFERENCES member (id),
                              CONSTRAINT fk_member_title_title FOREIGN KEY (title_id) REFERENCES title (id)
);

COMMENT ON TABLE member_title IS '사용자 칭호 보유 정보';
COMMENT ON COLUMN member_title.id IS '사용자 칭호 PK';
COMMENT ON COLUMN member_title.member_id IS '회원 PK';
COMMENT ON COLUMN member_title.title_id IS '칭호 PK';
COMMENT ON COLUMN member_title.selected IS '대표 칭호 여부';
COMMENT ON COLUMN member_title.created_at IS '생성일시';
COMMENT ON COLUMN member_title.updated_at IS '수정일시';


CREATE TABLE member_activity_log (
                                     id BIGSERIAL PRIMARY KEY,
                                     member_id BIGINT NOT NULL,
                                     activity_type VARCHAR(50) NOT NULL,
                                     exp_amount INT NOT NULL,
                                     description VARCHAR(255) NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_member_activity_log_member FOREIGN KEY (member_id) REFERENCES member (id),
                                     CONSTRAINT chk_member_activity_type CHECK (
                                         activity_type IN (
                                                           'BOOKMARK_ADD',
                                                           'PLACE_REVIEW_CREATE',
                                                           'ANCHOR_CREATE',
                                                           'ANCHOR_LIKE_RECEIVED',
                                                           'POPULAR_ANCHOR_SELECTED'
                                             )
                                         )
);

COMMENT ON TABLE member_activity_log IS '사용자 활동 로그';
COMMENT ON COLUMN member_activity_log.id IS '활동 로그 PK';
COMMENT ON COLUMN member_activity_log.member_id IS '회원 PK';
COMMENT ON COLUMN member_activity_log.activity_type IS '활동 타입';
COMMENT ON COLUMN member_activity_log.exp_amount IS '획득 경험치';
COMMENT ON COLUMN member_activity_log.description IS '활동 설명';
COMMENT ON COLUMN member_activity_log.created_at IS '생성일시';
COMMENT ON COLUMN member_activity_log.updated_at IS '수정일시';


CREATE INDEX idx_place_bookmarks_member_id ON place_bookmarks (member_id);
CREATE INDEX idx_place_bookmarks_place_id ON place_bookmarks (place_id);

CREATE INDEX idx_place_review_member_id ON place_review (member_id);
CREATE INDEX idx_place_review_place_id ON place_review (place_id);

CREATE INDEX idx_anchor_member_id ON anchor (member_id);
CREATE INDEX idx_anchor_base_place_id ON anchor (base_place_id);
CREATE INDEX idx_anchor_is_public ON anchor (is_public);
CREATE INDEX idx_anchor_view_count ON anchor (view_count);

CREATE INDEX idx_anchor_place_anchor_id ON anchor_place (anchor_id);
CREATE INDEX idx_anchor_place_place_id ON anchor_place (place_id);
CREATE INDEX idx_anchor_place_category ON anchor_place (category);

CREATE INDEX idx_anchor_like_member_id ON anchor_like (member_id);
CREATE INDEX idx_anchor_like_anchor_id ON anchor_like (anchor_id);

CREATE INDEX idx_member_achievement_member_id ON member_achievement (member_id);
CREATE INDEX idx_member_achievement_achievement_id ON member_achievement (achievement_id);

CREATE INDEX idx_member_title_member_id ON member_title (member_id);
CREATE INDEX idx_member_title_title_id ON member_title (title_id);

CREATE INDEX idx_member_activity_log_member_id ON member_activity_log (member_id);
CREATE INDEX idx_member_activity_log_activity_type ON member_activity_log (activity_type);