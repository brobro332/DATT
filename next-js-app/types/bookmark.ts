export type PlaceBookmarkResponse = {
  bookmarkId: number;
  placeId: number;

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

  bookmarkedAt: string;
};