package xyz.datt.domain.bookmark.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import xyz.datt.domain.bookmark.dto.PlaceBookmarkResponse;
import xyz.datt.domain.bookmark.entity.PlaceBookmark;
import xyz.datt.domain.bookmark.repository.PlaceBookmarkRepository;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;
import xyz.datt.global.error.BusinessException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceBookmarkServiceTest {
    private final PlaceBookmarkRepository placeBookmarkRepository = mock(PlaceBookmarkRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final PlaceMasterRepository placeMasterRepository = mock(PlaceMasterRepository.class);

    private final PlaceBookmarkService placeBookmarkService = new PlaceBookmarkService(
        placeBookmarkRepository,
        memberRepository,
        placeMasterRepository
    );

    @Test
    @DisplayName("장소 북마크를 추가한다.")
    void givenMemberAndPlace_whenAddBookmark_thenSaveBookmark() {
        Member member = createMember();
        PlaceMaster placeMaster = createPlaceMaster();
        PlaceBookmark bookmark = PlaceBookmark.builder()
            .member(member)
            .placeMaster(placeMaster)
            .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(placeMasterRepository.findById(10L)).thenReturn(Optional.of(placeMaster));
        when(placeBookmarkRepository.existsByMemberIdAndPlaceMasterId(1L, 10L)).thenReturn(false);
        when(placeBookmarkRepository.save(any(PlaceBookmark.class))).thenReturn(bookmark);

        PlaceBookmarkResponse response = placeBookmarkService.addBookmark(1L, 10L);

        assertThat(response).isNotNull();
        assertThat(response.bizesNm()).isEqualTo("스타벅스 강남점");
        assertThat(response.indsMclsCd()).isEqualTo("I212");

        verify(placeBookmarkRepository).save(any(PlaceBookmark.class));
    }

    @Test
    @DisplayName("이미 저장한 장소를 다시 북마크하면 예외가 발생한다.")
    void givenDuplicatedBookmark_whenAddBookmark_thenThrowException() {
        Member member = createMember();
        PlaceMaster placeMaster = createPlaceMaster();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(placeMasterRepository.findById(10L)).thenReturn(Optional.of(placeMaster));
        when(placeBookmarkRepository.existsByMemberIdAndPlaceMasterId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> placeBookmarkService.addBookmark(1L, 10L))
            .isInstanceOf(BusinessException.class);

        verify(placeBookmarkRepository, never()).save(any(PlaceBookmark.class));
    }

    @Test
    @DisplayName("장소 북마크를 삭제한다.")
    void givenBookmarkedPlace_whenRemoveBookmark_thenDeleteBookmark() {
        PlaceBookmark bookmark = PlaceBookmark.builder()
            .member(createMember())
            .placeMaster(createPlaceMaster())
            .build();

        when(placeBookmarkRepository.findByMemberIdAndPlaceMasterId(1L, 10L))
            .thenReturn(Optional.of(bookmark));

        placeBookmarkService.removeBookmark(1L, 10L);

        verify(placeBookmarkRepository).delete(bookmark);
    }

    @Test
    @DisplayName("저장하지 않은 장소 북마크를 삭제하면 예외가 발생한다.")
    void givenNotBookmarkedPlace_whenRemoveBookmark_thenThrowException() {
        when(placeBookmarkRepository.findByMemberIdAndPlaceMasterId(1L, 10L))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> placeBookmarkService.removeBookmark(1L, 10L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("내 장소 북마크 목록을 조회한다.")
    void givenMember_whenGetMyBookmarks_thenReturnBookmarks() {
        PlaceBookmark bookmark = PlaceBookmark.builder()
            .member(createMember())
            .placeMaster(createPlaceMaster())
            .build();

        PageRequest pageable = PageRequest.of(0, 10);
        Page<PlaceBookmark> page = new PageImpl<>(List.of(bookmark), pageable, 1);

        when(placeBookmarkRepository.findByMemberIdOrderByCreatedAtDesc(1L, pageable))
            .thenReturn(page);

        Page<PlaceBookmarkResponse> result = placeBookmarkService.getMyBookmarks(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).bizesNm()).isEqualTo("스타벅스 강남점");
    }

    @Test
    @DisplayName("장소 북마크 여부를 조회한다.")
    void givenMemberAndPlace_whenIsBookmarked_thenReturnTrue() {
        when(placeBookmarkRepository.existsByMemberIdAndPlaceMasterId(1L, 10L))
            .thenReturn(true);

        boolean result = placeBookmarkService.isBookmarked(1L, 10L);

        assertThat(result).isTrue();
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
            .bizesNm("스타벅스 강남점")
            .brchNm("강남점")
            .indsLclsCd("I2")
            .indsLclsNm("음식")
            .indsMclsCd("I212")
            .indsMclsNm("비알코올")
            .indsSclsCd("I21201")
            .indsSclsNm("카페")
            .ctprvnNm("서울특별시")
            .signguNm("강남구")
            .adongNm("역삼동")
            .ldongNm("역삼동")
            .lnoAdr("서울특별시 강남구 역삼동 123-1")
            .rdnmAdr("서울특별시 강남구 테헤란로 123")
            .newZipcd("06123")
            .lon(127.0276)
            .lat(37.4979)
            .location("POINT(127.0276 37.4979)")
            .build();
    }
}