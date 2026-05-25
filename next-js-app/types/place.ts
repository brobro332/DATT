export type PlaceSearchResponse = {
  id: number;
  bizesNm: string;
  brchNm: string | null;
  indsMclsCd: string;
  indsMclsNm: string;
  ctprvnNm: string;
  signguNm: string;
  adongNm: string;
  rdnmAdr: string;
  lon: number;
  lat: number;
};

export type PlaceSearchParams = {
  keyword?: string;
  page?: number;
  size?: number;
};

export type PlaceDetailResponse = {
  id: number;
  bizesId: string;

  bizesNm: string;
  brchNm: string | null;

  indsLclsCd: string;
  indsLclsNm: string;
  indsMclsCd: string;
  indsMclsNm: string;
  indsSclsCd: string;
  indsSclsNm: string;

  ctprvnNm: string;
  signguNm: string;
  adongNm: string;
  ldongNm: string;

  lnoAdr: string;
  rdnmAdr: string;
  newZipcd: string;

  lon: number;
  lat: number;

  isBookmarked: boolean;
  averageRating: number;
  reviewCount: number;

  createdAt: string;
  updatedAt: string;
};