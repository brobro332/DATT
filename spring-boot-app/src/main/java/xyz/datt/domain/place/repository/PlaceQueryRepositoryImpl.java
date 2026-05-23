package xyz.datt.domain.place.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import xyz.datt.domain.place.dto.PlaceNearbyCondition;
import xyz.datt.domain.place.dto.PlaceNearbyResponse;
import xyz.datt.domain.place.dto.PlaceSearchCondition;
import xyz.datt.domain.place.dto.PlaceSearchResponse;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.entity.PlaceSortType;

import java.util.List;

import static xyz.datt.domain.place.entity.QPlaceMaster.placeMaster;

@RequiredArgsConstructor
public class PlaceQueryRepositoryImpl implements PlaceQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PlaceSearchResponse> searchPlaces(
        PlaceSearchCondition condition,
        Pageable pageable
    ) {
        List<PlaceMaster> content = queryFactory
            .selectFrom(placeMaster)
            .where(
                keywordContains(condition.getKeyword()),
                ctprvnNmEq(condition.getCtprvnNm()),
                signguNmEq(condition.getSignguNm()),
                adongNmEq(condition.getAdongNm()),
                indsMclsCdEq(condition.getIndsMclsCd())
            )
            .orderBy(orderBy(condition.getSortType()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<PlaceSearchResponse> responses = content.stream()
            .map(PlaceSearchResponse::from)
            .toList();

        return PageableExecutionUtils.getPage(
            responses,
            pageable,
            () -> {
                Long count = queryFactory
                    .select(placeMaster.count())
                    .from(placeMaster)
                    .where(
                        keywordContains(condition.getKeyword()),
                        ctprvnNmEq(condition.getCtprvnNm()),
                        signguNmEq(condition.getSignguNm()),
                        adongNmEq(condition.getAdongNm()),
                        indsMclsCdEq(condition.getIndsMclsCd())
                    )
                    .fetchOne();

                return count == null ? 0L : count;
            }
        );
    }

    @Override
    public Page<PlaceNearbyResponse> searchNearbyPlaces(
        PlaceNearbyCondition condition,
        Pageable pageable
    ) {
        NumberExpression<Double> distanceExpression = distanceExpression(
            condition.getLat(),
            condition.getLon()
        );

        List<PlaceNearbyResponse> content = queryFactory
            .select(Projections.constructor(
                PlaceNearbyResponse.class,
                placeMaster.id,
                placeMaster.bizesNm,
                placeMaster.brchNm,
                placeMaster.indsMclsCd,
                placeMaster.indsMclsNm,
                placeMaster.ctprvnNm,
                placeMaster.signguNm,
                placeMaster.adongNm,
                placeMaster.rdnmAdr,
                placeMaster.lon,
                placeMaster.lat,
                distanceExpression
            ))
            .from(placeMaster)
            .where(
                placeMaster.lon.isNotNull(),
                placeMaster.lat.isNotNull(),
                withinRadius(distanceExpression, condition.getRadiusKm()),
                keywordContains(condition.getKeyword()),
                indsMclsCdEq(condition.getIndsMclsCd())
            )
            .orderBy(orderByDistance(distanceExpression))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory
            .select(placeMaster.count())
            .from(placeMaster)
            .where(
                placeMaster.lon.isNotNull(),
                placeMaster.lat.isNotNull(),
                withinRadius(distanceExpression, condition.getRadiusKm()),
                keywordContains(condition.getKeyword()),
                indsMclsCdEq(condition.getIndsMclsCd())
            )
            .fetchOne();

        return PageableExecutionUtils.getPage(
            content,
            pageable,
            () -> count == null ? 0L : count
        );
    }

    @Override
    public List<PlaceNearbyResponse> findNearbyPlacesForAnchor(
        Double baseLat,
        Double baseLon,
        Double radiusKm,
        List<String> indsMclsCodes,
        int limit
    ) {
        NumberExpression<Double> distanceExpression = distanceExpression(
            baseLat,
            baseLon
        );

        return queryFactory
            .select(Projections.constructor(
                PlaceNearbyResponse.class,
                placeMaster.id,
                placeMaster.bizesNm,
                placeMaster.brchNm,
                placeMaster.indsMclsCd,
                placeMaster.indsMclsNm,
                placeMaster.ctprvnNm,
                placeMaster.signguNm,
                placeMaster.adongNm,
                placeMaster.rdnmAdr,
                placeMaster.lon,
                placeMaster.lat,
                distanceExpression
            ))
            .from(placeMaster)
            .where(
                placeMaster.lon.isNotNull(),
                placeMaster.lat.isNotNull(),
                withinRadius(distanceExpression, radiusKm),
                placeMaster.indsMclsCd.in(indsMclsCodes)
            )
            .orderBy(distanceExpression.asc())
            .limit(limit)
            .fetch();
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return placeMaster.bizesNm.containsIgnoreCase(keyword)
                .or(placeMaster.brchNm.containsIgnoreCase(keyword));
    }

    private BooleanExpression ctprvnNmEq(String ctprvnNm) {
        if (ctprvnNm == null || ctprvnNm.isBlank()) {
            return null;
        }

        return placeMaster.ctprvnNm.eq(ctprvnNm);
    }

    private BooleanExpression signguNmEq(String signguNm) {
        if (signguNm == null || signguNm.isBlank()) {
            return null;
        }

        return placeMaster.signguNm.eq(signguNm);
    }

    private BooleanExpression adongNmEq(String adongNm) {
        if (adongNm == null || adongNm.isBlank()) {
            return null;
        }

        return placeMaster.adongNm.eq(adongNm);
    }

    private BooleanExpression indsMclsCdEq(String indsMclsCd) {
        if (indsMclsCd == null || indsMclsCd.isBlank()) {
            return null;
        }

        return placeMaster.indsMclsCd.eq(indsMclsCd);
    }

    private OrderSpecifier<?> orderBy(PlaceSortType sortType) {
        return switch (sortType) {
            case NAME -> placeMaster.bizesNm.asc();
            case LATEST -> placeMaster.createdAt.desc();
        };
    }

    private NumberExpression<Double> distanceExpression(
        Double baseLat,
        Double baseLon
    ) {
        return Expressions.numberTemplate(
            Double.class,
            """
            (6371 * acos(
                cos(radians({0})) * cos(radians({1})) *
                cos(radians({2}) - radians({3})) +
                sin(radians({0})) * sin(radians({1}))
            ))
            """,
            baseLat,
            placeMaster.lat,
            placeMaster.lon,
            baseLon
        );
    }

    private BooleanExpression withinRadius(
        NumberExpression<Double> distanceExpression,
        Double radiusKm
    ) {
        if (radiusKm == null) {
            return null;
        }

        return distanceExpression.loe(radiusKm);
    }

    private OrderSpecifier<Double> orderByDistance(
        NumberExpression<Double> distanceExpression
    ) {
        return distanceExpression.asc();
    }
}