"use client";

import { useEffect, useRef } from "react";
import { env } from "@/lib/env";

type MapContainerProps = {
    lat?: number;
    lon?: number;
    level?: number;
    className?: string;
    onMapLoad?: (map: kakao.maps.Map) => void;
};

const DEFAULT_LAT = 37.5665;
const DEFAULT_LON = 126.978;
const DEFAULT_LEVEL = 5;

export function MapContainer({
    lat = DEFAULT_LAT,
    lon = DEFAULT_LON,
    level = DEFAULT_LEVEL,
    className = "",
    onMapLoad,
}: MapContainerProps) {
    const mapRef = useRef<HTMLDivElement | null>(null);
    const mapInstanceRef = useRef<kakao.maps.Map | null>(null);

    useEffect(() => {
        if (!mapRef.current) {
            return;
        }

        function createMap() {
            if (!mapRef.current) {
                return;
            }

            const center = new window.kakao.maps.LatLng(lat, lon);

            const map = new window.kakao.maps.Map(mapRef.current, {
                center,
                level,
            });

            mapInstanceRef.current = map;
            onMapLoad?.(map);
        }

        if (window.kakao?.maps) {
            window.kakao.maps.load(createMap);
            return;
        }

        const script = document.createElement("script");

        script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${env.kakaoMapAppKey}&autoload=false`;
        script.async = true;

        script.onload = () => {
            window.kakao.maps.load(createMap);
        };

        document.head.appendChild(script);
    }, [lat, lon, level, onMapLoad]);

    return (
        <div
            ref={mapRef}
            className={[
                "h-[520px] w-full overflow-hidden rounded-3xl border border-gray-200 bg-gray-100",
                className,
            ].join(" ")}
        />
    );
}