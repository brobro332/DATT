package xyz.datt.domain.place.batch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "public-data.place")
public class PlacePublicDataProperties {
    private String baseUrl;
    private String endpoint;
    private String serviceKey;
    private int numOfRows;
    private String type;
}