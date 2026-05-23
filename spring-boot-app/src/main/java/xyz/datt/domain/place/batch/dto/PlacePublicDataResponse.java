package xyz.datt.domain.place.batch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlacePublicDataResponse {

    private Header header;
    private Body body;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Header {
        private String description;
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Body {
        private List<PlacePublicDataItem> items;
    }
}