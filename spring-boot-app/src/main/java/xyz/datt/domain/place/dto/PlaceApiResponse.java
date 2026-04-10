package xyz.datt.domain.place.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaceApiResponse {
    private Header header;
    private Body body;

    @Getter @Setter
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter @Setter
    public static class Body {
        private List<PlaceResponseDto> items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }
}