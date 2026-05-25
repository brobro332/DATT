import { useQuery } from "@tanstack/react-query";
import { searchPlaces } from "@/services/placeService";
import type { PlaceSearchParams } from "@/types/place";

export function usePlaceSearch(params: PlaceSearchParams) {
  return useQuery({
    queryKey: ["places", params],
    queryFn: () => searchPlaces(params),
    enabled: Boolean(params.keyword),
  });
}