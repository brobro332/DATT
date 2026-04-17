package xyz.datt.domain.place.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import xyz.datt.domain.place.entity.Place;
import xyz.datt.domain.place.entity.Platform;
import xyz.datt.domain.place.entity.QPlace;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Place> searchPlaces(String keyword, String category, Platform platform, Pageable pageable) {
        QPlace place = QPlace.place;
        Pageable securePageable = (pageable == null || pageable.isUnpaged())
            ? PageRequest.of(0, 20)
            : pageable;

        List<Place> content = queryFactory
            .selectFrom(place)
            .where(
                containsKeyword(keyword, place),
                eqCategory(category, place),
                eqPlatform(platform, place)
            )
            .offset(securePageable.getOffset())
            .limit(securePageable.getPageSize())
            .orderBy(place.reviewCount.desc())
            .fetch();

        Long total = queryFactory
            .select(place.count())
            .from(place)
            .where(
                containsKeyword(keyword, place),
                eqCategory(category, place),
                eqPlatform(platform, place)
            )
            .fetchOne();

        return new PageImpl<>(content, securePageable, total != null ? total : 0L);
    }

    private BooleanExpression containsKeyword(String keyword, QPlace place) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return place.keyword.contains(keyword).or(place.address.contains(keyword));
    }

    private BooleanExpression eqCategory(String category, QPlace place) {
        return (category == null || category.isBlank() || category.equals("전체")) ? null : place.category.eq(category);
    }

    private BooleanExpression eqPlatform(Platform platform, QPlace place) {
        return (platform == null) ? null : place.platform.eq(platform);
    }
}