declare global {
  interface Window {
    kakao: typeof kakao;
  }
}

declare namespace kakao.maps {
  function load(callback: () => void): void;

  class LatLng {
    constructor(lat: number, lng: number);
  }

  class Map {
    constructor(container: HTMLElement, options: MapOptions);
  }

  type MapOptions = {
    center: LatLng;
    level: number;
  };
}

export {};