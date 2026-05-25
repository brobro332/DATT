"use client";

import { useEffect, useRef } from "react";

type CurrentLocationMarkerProps = {
    map: kakao.maps.Map | null;
    lat: number | null;
    lon: number | null;
};

export function CurrentLocationMarker({
    map,
    lat,
    lon,
}: CurrentLocationMarkerProps) {
    const markerRef = useRef<kakao.maps.Marker | null>(null);

    useEffect(() => {
        if (!map || lat === null || lon === null) {
            markerRef.current?.setMap(null);
            return;
        }

        const position = new window.kakao.maps.LatLng(lat, lon);

        if (!markerRef.current) {
            markerRef.current = new window.kakao.maps.Marker({
                map,
                position,
                title: "현재 위치",
            });

            return;
        }

        markerRef.current.setPosition(position);
        markerRef.current.setMap(map);

        return () => {
            markerRef.current?.setMap(null);
        };
    }, [map, lat, lon]);

    return null;
}