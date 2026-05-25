import Link from "next/link";
import type { PlaceSearchResponse } from "@/types/place";

type PlaceListItemProps = {
  place: PlaceSearchResponse;
};

export function PlaceListItem({ place }: PlaceListItemProps) {
  return (
    <li className="border-b border-gray-100 bg-white px-4 py-4 transition hover:bg-gray-50">
      <div className="flex items-center justify-between gap-4">
        <div className="min-w-0 flex-1">
          <div className="mb-1 flex items-center gap-2">
            <span className="rounded-full bg-gray-100 px-2.5 py-1 text-xs font-medium text-gray-600">
              {place.indsMclsNm || "카테고리"}
            </span>

            <span className="text-xs text-gray-400">
              {place.ctprvnNm} {place.signguNm} {place.adongNm}
            </span>
          </div>

          <h3 className="truncate text-base font-bold text-gray-950">
            {place.bizesNm}
            {place.brchNm && (
              <span className="ml-1 text-sm font-medium text-gray-500">
                {place.brchNm}
              </span>
            )}
          </h3>

          <p className="mt-1 truncate text-sm text-gray-600">
            {place.rdnmAdr || "주소 정보 없음"}
          </p>
        </div>

        <Link
          href={`/places/${place.id}`}
          className="shrink-0 rounded-full border border-gray-200 px-4 py-2 text-sm font-semibold text-gray-700 transition hover:border-gray-900 hover:text-gray-950"
        >
          상세
        </Link>
      </div>
    </li>
  );
}