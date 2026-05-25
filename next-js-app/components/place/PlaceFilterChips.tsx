const filters = [
  "전체",
  "맛집",
  "카페",
  "술집",
  "숙소",
  "놀거리",
];

export function PlaceFilterChips() {
  return (
    <div className="flex flex-wrap gap-2">
      {filters.map((filter) => (
        <button
          key={filter}
          type="button"
          className="rounded-full border border-gray-200 bg-white px-4 py-2 text-sm font-medium text-gray-600 transition hover:border-gray-900 hover:text-gray-950"
        >
          {filter}
        </button>
      ))}
    </div>
  );
}