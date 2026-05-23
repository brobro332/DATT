"use client";

import { useEffect, useRef } from "react";
import { env } from "@/lib/env";

export function KakaoMap() {
  const mapRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (!mapRef.current) {
      return;
    }

    if (window.kakao?.maps) {
      window.kakao.maps.load(() => {
        createMap();
      });

      return;
    }

    const script = document.createElement("script");

    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${env.kakaoMapAppKey}&autoload=false`;
    script.async = true;

    script.onload = () => {
      window.kakao.maps.load(() => {
        createMap();
      });
    };

    document.head.appendChild(script);

    function createMap() {
      if (!mapRef.current) {
        return;
      }

      const center = new window.kakao.maps.LatLng(
        37.5665,
        126.978,
      );

      new window.kakao.maps.Map(mapRef.current, {
        center,
        level: 5,
      });
    }
  }, []);

  return (
    <div className="h-[520px] w-full overflow-hidden rounded-3xl border border-gray-200 bg-gray-100" ref={mapRef} />
  );
}