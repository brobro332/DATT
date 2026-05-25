declare namespace kakao.maps {
    function load(callback: () => void): void;

    class LatLng {
        constructor(lat: number, lng: number);
    }

    type MapOptions = {
        center: LatLng;
        level: number;
    };

    class Map {
        constructor(container: HTMLElement, options: MapOptions);

        setCenter(latlng: LatLng): void;
        setLevel(level: number): void;
    }

    class Marker {
        constructor(options: MarkerOptions);

        setMap(map: Map | null): void;
        setPosition(position: LatLng): void;
    }

    class Map {
        constructor(container: HTMLElement, options: MapOptions);

        setCenter(latlng: LatLng): void;
        setLevel(level: number): void;
    }

    type MarkerOptions = {
        map?: Map;
        position: LatLng;
        title?: string;
    };

    class CustomOverlay {
        constructor(options: CustomOverlayOptions);

        setMap(map: Map | null): void;
        setPosition(position: LatLng): void;
        setContent(content: string | HTMLElement): void;
    }

    type CustomOverlayOptions = {
        map?: Map;
        position: LatLng;
        content: string | HTMLElement;
        yAnchor?: number;
        xAnchor?: number;
        zIndex?: number;
    };

    namespace event {
        function addListener(
            target: unknown,
            type: string,
            handler: () => void,
        ): void;
    }
}

interface Window {
    kakao: typeof kakao;
}