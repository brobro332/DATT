"use client";

import { useState } from "react";

import { MainLayout } from "@/layouts/MainLayout";

import { AsyncStateView } from "@/components/common/AsyncStateView";
import { Button } from "@/components/common/Button";
import { MapContainer } from "@/components/map/MapContainer";
import { PlaceMarkers } from "@/components/map/PlaceMarkers";
import { PlaceOverlay } from "@/components/map/PlaceOverlay";
import { PlaceSearchForm } from "@/components/place/PlaceSearchForm";
import { PlaceListItem } from "@/components/place/PlaceListItem";

import { usePlaceSearch } from "@/hooks/usePlaceSearch";

import type { PlaceSearchResponse } from "@/types/place";
import { CurrentLocationMarker } from "@/components/map/CurrentLocationMaker";

const PAGE_SIZE = 10;

export default function MapPage() {
    const [keyword, setKeyword] = useState("");
    const [map, setMap] = useState<kakao.maps.Map | null>(null);
    const [selectedPlace, setSelectedPlace] =
        useState<PlaceSearchResponse | null>(null);

    const [currentLocation, setCurrentLocation] = useState<{
        lat: number;
        lon: number;
    } | null>(null);

    const [isLocating, setIsLocating] = useState(false);

    const { data, isLoading, isError } = usePlaceSearch({
        keyword,
        page: 0,
        size: PAGE_SIZE,
    });

    const places = data?.content ?? [];

    function handleSearch(nextKeyword: string) {
        setKeyword(nextKeyword.trim());
        setSelectedPlace(null);
    }

    function handlePlaceSelect(place: PlaceSearchResponse) {
        setSelectedPlace(place);

        if (!map) {
            return;
        }

        const position = new window.kakao.maps.LatLng(
            place.lat,
            place.lon,
        );

        map.setCenter(position);
    }

    function handleCurrentLocation() {
        if (!navigator.geolocation) {
            alert("현재 브라우저에서는 위치 정보를 사용할 수 없습니다.");
            return;
        }

        setIsLocating(true);

        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;

                setCurrentLocation({
                    lat,
                    lon,
                });

                if (map) {
                    const latLng = new window.kakao.maps.LatLng(lat, lon);

                    map.setCenter(latLng);
                    map.setLevel(4);
                }

                setIsLocating(false);
            },
            () => {
                alert("현재 위치를 가져오지 못했습니다.");
                setIsLocating(false);
            },
            {
                enableHighAccuracy: true,
                timeout: 10000,
                maximumAge: 1000 * 60,
            },
        );
    }

    return (
        <MainLayout>
            <section className="space-y-6">
                <div className="flex flex-col justify-between gap-4 md:flex-row md:items-end">
                    <div>
                        <p className="text-sm font-semibold text-gray-500">
                            Map Search
                        </p>

                        <h1 className="mt-2 text-3xl font-bold tracking-tight text-gray-950">
                            지도에서 장소를 탐색해보세요
                        </h1>

                        <p className="mt-3 text-sm leading-6 text-gray-600">
                            검색한 장소를 지도 위에서 확인하고, 결과 리스트와 함께 비교할 수 있습니다.
                        </p>
                    </div>

                    <Button
                        type="button"
                        variant="secondary"
                        onClick={handleCurrentLocation}
                        disabled={isLocating}
                    >
                        {isLocating ? "위치 확인 중..." : "현재 위치"}
                    </Button>
                </div>

                <div className="rounded-3xl border border-gray-200 bg-white p-4 shadow-sm">
                    <PlaceSearchForm onSearch={handleSearch} />
                </div>

                <div className="grid gap-6 lg:grid-cols-[minmax(0,1.45fr)_420px]">
                    <div className="relative overflow-hidden rounded-3xl border border-gray-200 bg-gray-100 shadow-sm">
                        <MapContainer
                            className="h-[680px] rounded-3xl border-0"
                            onMapLoad={setMap}
                        />

                        <PlaceMarkers
                            map={map}
                            places={places}
                            onMarkerClick={handlePlaceSelect}
                        />

                        <PlaceOverlay
                            map={map}
                            place={selectedPlace}
                        />

                        <CurrentLocationMarker
                            map={map}
                            lat={currentLocation?.lat ?? null}
                            lon={currentLocation?.lon ?? null}
                        />

                        <div className="pointer-events-none absolute left-4 top-4 z-20 rounded-2xl bg-white/90 px-4 py-3 text-sm shadow-md backdrop-blur">
                            <p className="font-semibold text-gray-950">
                                {selectedPlace
                                    ? selectedPlace.bizesNm
                                    : "장소를 선택해보세요"}
                            </p>

                            <p className="mt-1 text-xs text-gray-500">
                                {selectedPlace
                                    ? selectedPlace.rdnmAdr
                                    : "마커 또는 리스트를 클릭하면 상세 정보가 표시됩니다."}
                            </p>
                        </div>
                    </div>

                    <aside className="rounded-3xl border border-gray-200 bg-white shadow-sm">
                        <div className="border-b border-gray-100 p-5">
                            <div className="flex items-center justify-between">
                                <div>
                                    <h2 className="text-lg font-bold text-gray-950">
                                        검색 결과
                                    </h2>

                                    <p className="mt-1 text-sm text-gray-500">
                                        {data
                                            ? `${data.totalElements.toLocaleString()}개 결과`
                                            : "검색어를 입력해주세요."}
                                    </p>
                                </div>

                                {selectedPlace && (
                                    <span className="rounded-full bg-gray-100 px-3 py-1 text-xs font-semibold text-gray-600">
                                        선택됨
                                    </span>
                                )}
                            </div>
                        </div>

                        <div className="p-4">
                            <AsyncStateView
                                isLoading={isLoading}
                                isError={isError}
                                isEmpty={Boolean(data?.empty)}
                                loadingMessage="장소를 검색하는 중입니다..."
                                errorTitle="장소 검색 실패"
                                errorMessage="장소 검색 중 문제가 발생했습니다."
                                emptyTitle="검색 결과가 없습니다."
                                emptyDescription="다른 키워드로 다시 검색해보세요."
                            >
                                <ul className="max-h-[560px] overflow-y-auto rounded-2xl border border-gray-100 bg-white">
                                    {places.map((place) => (
                                        <PlaceListItem
                                            key={place.id}
                                            place={place}
                                            isSelected={selectedPlace?.id === place.id}
                                            onClick={handlePlaceSelect}
                                        />
                                    ))}
                                </ul>
                            </AsyncStateView>
                        </div>
                    </aside>
                </div>
            </section>
        </MainLayout>
    );
}