package xyz.datt.domain.place.util;

public final class DistanceCalculator {
    private static final double EARTH_RADIUS_KM = 6371.0;

    private DistanceCalculator() {
    }

    public static double calculateDistanceKm(
        double baseLat,
        double baseLon,
        double targetLat,
        double targetLon
    ) {
        double baseLatRad = Math.toRadians(baseLat);
        double targetLatRad = Math.toRadians(targetLat);

        double deltaLat = Math.toRadians(targetLat - baseLat);
        double deltaLon = Math.toRadians(targetLon - baseLon);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
            + Math.cos(baseLatRad)
            * Math.cos(targetLatRad)
            * Math.sin(deltaLon / 2)
            * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}