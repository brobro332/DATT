package xyz.datt.domain.place.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.datt.domain.bookmark.service.PlaceBookmarkService;
import xyz.datt.domain.place.dto.PlaceDetailResponse;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;
import xyz.datt.domain.review.service.PlaceReviewService;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlaceDetailServiceTest {
    private final PlaceMasterRepository placeMasterRepository =
        mock(PlaceMasterRepository.class);

    private final PlaceBookmarkService placeBookmarkService =
        mock(PlaceBookmarkService.class);

    private final PlaceReviewService placeReviewService =
        mock(PlaceReviewService.class);

    private final PlaceDetailService placeDetailService =
        new PlaceDetailService(placeMasterRepository, placeBookmarkService, placeReviewService);

    @Test
    @DisplayName("장소 ID로 장소 상세 정보를 조회한다.")
    void givenPlaceId_whenGetPlaceDetail_thenReturnPlaceDetail() {
        PlaceMaster placeMaster = createPlaceMaster();

        when(placeMasterRepository.findById(1L))
            .thenReturn(Optional.of(placeMaster));

        PlaceDetailResponse response = placeDetailService.getPlaceDetail(1L);

        assertThat(response).isNotNull();
        assertThat(response.bizesId()).isEqualTo("BIZ-001");
        assertThat(response.bizesNm()).isEqualTo("스타벅스 강남점");
        assertThat(response.indsMclsCd()).isEqualTo("I212");
        assertThat(response.indsMclsNm()).isEqualTo("비알코올");
        assertThat(response.ctprvnNm()).isEqualTo("서울특별시");
        assertThat(response.signguNm()).isEqualTo("강남구");
        assertThat(response.rdnmAdr()).isEqualTo("서울특별시 강남구 테헤란로 123");
    }

    @Test
    @DisplayName("존재하지 않는 장소 ID로 조회하면 예외가 발생한다.")
    void givenNotFoundPlaceId_whenGetPlaceDetail_thenThrowException() {
        when(placeMasterRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> placeDetailService.getPlaceDetail(999L))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.PLACE_NOT_FOUND.getMessage());
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