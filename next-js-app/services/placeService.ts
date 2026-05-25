import { apiClient } from "@/lib/apiClient";
import type { ApiResponse, PageResponse } from "@/types/api";
import type {
  PlaceDetailResponse,
  PlaceSearchParams,
  PlaceSearchResponse,
} from "@/types/place";

export async function searchPlaces(
  params: PlaceSearchParams,
): Promise<PageResponse<PlaceSearchResponse>> {
  const response = await apiClient.get<
    ApiResponse<PageResponse<PlaceSearchResponse>>
  >("/api/places", {
    params: {
      keyword: params.keyword,
      page: params.page ?? 0,
      size: params.size ?? 10,
    },
  });

  return response.data.data;
}

export async function getPlaceDetail(
  placeId: number,
): Promise<PlaceDetailResponse> {
  const response = await apiClient.get<ApiResponse<PlaceDetailResponse>>(
    `/api/places/${placeId}`,
  );

  return response.data.data;
}