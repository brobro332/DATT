package xyz.datt.domain.anchor.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.datt.domain.anchor.dto.AnchorDetailResponse;
import xyz.datt.domain.anchor.dto.AnchorPlaceGroupResponse;
import xyz.datt.domain.anchor.dto.AnchorPlaceResponse;
import xyz.datt.domain.anchor.entity.Anchor;
import xyz.datt.domain.anchor.entity.AnchorPlace;
import xyz.datt.domain.anchor.entity.AnchorPlaceCategory;
import xyz.datt.domain.anchor.repository.AnchorPlaceRepository;
import xyz.datt.domain.anchor.repository.AnchorRepository;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.global.error.BusinessException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnchorDetailServiceTest {
    private final AnchorRepository anchorRepository =
        mock(AnchorRepository.class);

    private final AnchorPlaceRepository anchorPlaceRepository =
        mock(AnchorPlaceRepository.class);

    private final AnchorLikeService anchorLikeService =
        mock(AnchorLikeService.class);

    private final AnchorDetailService anchorDetailService =
        new AnchorDetailService(
            anchorRepository,
            anchorPlaceRepository,
            anchorLikeService
        );

    @Test
    @DisplayName("공개 Anchor 상세 조회에 성공한다.")
    void givenPublicAnchor_whenGetAnchorDetail_thenReturnDetail() {
        Member member = createMember();

        Anchor anchor = Anchor.builder()
            .member(member)
            .title("성수 감성 데이트")
            .basePlaceName("성수역")
            .baseAddress("서울특별시 성동구 성수동")
            .baseLon(127.0557)
            .baseLat(37.5446)
            .radiusKm(3.0)
            .isPublic(true)
            .build();

        PlaceMaster placeMaster = createPlaceMaster();

        AnchorPlace anchorPlace = AnchorPlace.builder()
            .anchor(anchor)
            .placeMaster(placeMaster)
            .category(AnchorPlaceCategory.CAFE)
            .distanceKm(0.5)
            .recommendOrder(1)
            .build();

        when(anchorRepository.findById(1L))
            .thenReturn(Optional.of(anchor));

        when(anchorPlaceRepository
            .findByAnchorIdOrderByCategoryAscRecommendOrderAsc(1L))
            .thenReturn(List.of(anchorPlace));

        AnchorDetailResponse response =
            anchorDetailService.getAnchorDetail(null, 1L);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("성수 감성 데이트");
        assertThat(response.placeGroups()).hasSize(5);

        AnchorPlaceGroupResponse cafeGroup =
            response.placeGroups().stream()
                .filter(group -> group.category() == AnchorPlaceCategory.CAFE)
                .findFirst()
                .orElseThrow();

        assertThat(cafeGroup.places()).hasSize(1);

        AnchorPlaceResponse placeResponse = cafeGroup.places().get(0);

        assertThat(placeResponse.bizesNm())
            .isEqualTo("스타벅스 성수점");
    }

    @Test
    @DisplayName("비공개 Anchor는 작성자만 조회 가능하다.")
    void givenPrivateAnchor_whenOtherMemberAccess_thenThrowException() {
        Member owner = createMember();

        Anchor anchor = Anchor.builder()
            .member(owner)
            .title("비공개 Anchor")
            .basePlaceName("성수역")
            .baseAddress("서울특별시 성동구 성수동")
            .baseLon(127.0557)
            .baseLat(37.5446)
            .radiusKm(3.0)
            .isPublic(false)
            .build();

        when(anchorRepository.findById(1L))
            .thenReturn(Optional.of(anchor));

        assertThatThrownBy(() ->
            anchorDetailService.getAnchorDetail(999L, 1L)
        ).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Anchor 조회 시 조회수가 증가한다.")
    void givenAnchor_whenGetAnchorDetail_thenIncreaseViewCount() {
        Member member = createMember();

        Anchor anchor = Anchor.builder()
            .member(member)
            .title("성수 감성 데이트")
            .basePlaceName("성수역")
            .baseAddress("서울특별시 성동구 성수동")
            .baseLon(127.0557)
            .baseLat(37.5446)
            .radiusKm(3.0)
            .isPublic(true)
            .build();

        when(anchorRepository.findById(1L))
            .thenReturn(Optional.of(anchor));

        when(anchorPlaceRepository
            .findByAnchorIdOrderByCategoryAscRecommendOrderAsc(1L))
            .thenReturn(List.of());

        long before = anchor.getViewCount();

        anchorDetailService.getAnchorDetail(null, 1L);

        assertThat(anchor.getViewCount())
            .isEqualTo(before + 1);
    }

    private Member createMember() {
        return Member.createUser(
            "test@test.com",
            "encodedPassword",
            "bro"
        );
    }

    private PlaceMaster createPlaceMaster() {
        return PlaceMaster.builder()
            .bizesId("BIZ-001")
            .bizesNm("스타벅스 성수점")
            .brchNm("성수점")
            .indsMclsCd("I212")
            .indsMclsNm("카페")
            .rdnmAdr("서울특별시 성동구 성수동")
            .lon(127.0560)
            .lat(37.5450)
            .location("POINT(127.0560 37.5450)")
            .build();
    }
}