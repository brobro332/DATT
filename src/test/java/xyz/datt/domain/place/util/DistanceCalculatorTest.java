package xyz.datt.domain.place.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceCalculatorTest {
    @Test
    void givenSameCoordinate_whenCalculateDistance_thenReturnZero() {
        double distance = DistanceCalculator.calculateDistanceKm(
            37.4979,
            127.0276,
            37.4979,
            127.0276
        );

        assertThat(distance).isZero();
    }

    @Test
    void givenGangnamAndHongdae_whenCalculateDistance_thenReturnApproxDistance() {
        double distance = DistanceCalculator.calculateDistanceKm(
            37.4979,
            127.0276,
            37.5575,
            126.9250
        );

        assertThat(distance).isBetween(10.0, 13.0);
    }
}