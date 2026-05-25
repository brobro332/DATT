import { apiClient } from "@/lib/apiClient";
import type { ApiResponse, PageResponse } from "@/types/api";
import { PlaceBookmarkResponse } from "@/types/bookmark";

export async function addPlaceBookmark(
  placeId: number,
): Promise<PlaceBookmarkResponse> {
  const response = await apiClient.post<ApiResponse<PlaceBookmarkResponse>>(
    `/api/bookmarks/places/${placeId}`,
  );

  return response.data.data;
}

export async function removePlaceBookmark(
  placeId: number,
): Promise<void> {
  await apiClient.delete<ApiResponse<null>>(
    `/api/bookmarks/places/${placeId}`,
  );
}

export async function getMyPlaceBookmarks(
  page = 0,
  size = 10,
): Promise<PageResponse<PlaceBookmarkResponse>> {
  const response = await apiClient.get<
    ApiResponse<PageResponse<PlaceBookmarkResponse>>
  >("/api/bookmarks/places", {
    params: {
      page,
      size,
    },
  });

  return response.data.data;
}