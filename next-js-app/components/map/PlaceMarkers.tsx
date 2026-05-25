"use client";

import { useEffect, useRef } from "react";
import type { PlaceSearchResponse } from "@/types/place";

type PlaceMarkersProps = {
    map: kakao.maps.Map | null;
    places: PlaceSearchResponse[];
    onMarkerClick?: (place: PlaceSearchResponse) => void;
};

export function PlaceMarkers({
    map,
    places,
    onMarkerClick,
}: PlaceMarkersProps) {
    const markersRef = useRef<kakao.maps.Marker[]>([]);

    useEffect(() => {
        if (!map) {
            return;
        }

        markersRef.current.forEach((marker) => {
            marker.setMap(null);
        });

        markersRef.current = [];

        places.forEach((place) => {
            const markerPosition = new window.kakao.maps.LatLng(
                place.lat,
                place.lon,
            );

            const marker = new window.kakao.maps.Marker({
                map,
                position: markerPosition,
                title: place.bizesNm,
            });

            window.kakao.maps.event.addListener(marker, "click", () => {
                onMarkerClick?.(place);
            });

            markersRef.current.push(marker);
        });

        return () => {
            markersRef.current.forEach((marker) => {
                marker.setMap(null);
            });

            markersRef.current = [];
        };
    }, [map, places, onMarkerClick]);

    return null;
}