import { useQuery } from "@tanstack/react-query";
import { getPlaceDetail } from "@/services/placeService";

export function usePlaceDetail(placeId: number) {
  return useQuery({
    queryKey: ["place", placeId],
    queryFn: () => getPlaceDetail(placeId),
    enabled: Number.isFinite(placeId),
  });
}