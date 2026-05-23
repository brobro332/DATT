package xyz.datt.domain.place.batch.client;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.place.batch.dto.PlacePublicDataItem;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class PlacePublicDataClientTest {
    @Autowired
    private PlacePublicDataClient placePublicDataClient;

    private static final Dotenv dotenv = Dotenv.configure()
        .ignoreIfMissing()
        .load();

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add(
            "public-data.place.service-key",
            () -> dotenv.get("PUBLIC_DATA_SERVICE_KEY")
        );
    }


    @Test
    void givenMiddleCategory_whenFetchPlaces_thenReturnItems() {
        List<PlacePublicDataItem> items =
            placePublicDataClient.fetchPlaces(
                "indsMclsCd",
                "I212",
                1
            );

        assertThat(items).isNotEmpty();
    }
}