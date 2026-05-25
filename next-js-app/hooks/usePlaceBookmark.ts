import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  addPlaceBookmark,
  removePlaceBookmark,
} from "@/services/bookmarkService";

export function useAddPlaceBookmark(placeId: number) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => addPlaceBookmark(placeId),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["place", placeId],
      });

      queryClient.invalidateQueries({
        queryKey: ["my-place-bookmarks"],
      });
    },
  });
}

export function useRemovePlaceBookmark(placeId: number) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => removePlaceBookmark(placeId),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["place", placeId],
      });

      queryClient.invalidateQueries({
        queryKey: ["my-place-bookmarks"],
      });
    },
  });
}