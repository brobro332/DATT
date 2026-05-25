"use client";

import { useEffect, useRef } from "react";
import type { PlaceSearchResponse } from "@/types/place";

type PlaceOverlayProps = {
    map: kakao.maps.Map | null;
    place: PlaceSearchResponse | null;
};

export function PlaceOverlay({ map, place }: PlaceOverlayProps) {
    const overlayRef = useRef<kakao.maps.CustomOverlay | null>(null);

    useEffect(() => {
        if (!map || !place) {
            overlayRef.current?.setMap(null);
            return;
        }

        const position = new window.kakao.maps.LatLng(
            place.lat,
            place.lon,
        );

        const content = document.createElement("div");

        content.className =
            "min-w-64 rounded-2xl border border-gray-200 bg-white p-4 shadow-xl";

        content.innerHTML = `
            <div>
                <p style="font-size:12px;font-weight:600;color:#6b7280;">
                    ${place.indsMclsNm || "카테고리"}
                </p>

                <p style="margin-top:4px;font-size:16px;font-weight:700;color:#111827;">
                    ${place.bizesNm}
                </p>

                <p style="margin-top:6px;font-size:13px;line-height:1.5;color:#4b5563;">
                    ${place.rdnmAdr || "주소 정보 없음"}
                </p>

                <a
                    href="/places/${place.id}"
                    style="display:inline-block;margin-top:10px;font-size:13px;font-weight:700;color:#111827;text-decoration:underline;"
                >
                    상세 보기
                </a>
            </div>
        `;

        if (!overlayRef.current) {
            overlayRef.current = new window.kakao.maps.CustomOverlay({
                map,
                position,
                content,
                yAnchor: 1.35,
                zIndex: 10,
            });
        } else {
            overlayRef.current.setPosition(position);
            overlayRef.current.setContent(content);
            overlayRef.current.setMap(map);
        }

        return () => {
            overlayRef.current?.setMap(null);
        };
    }, [map, place]);

    return null;
}