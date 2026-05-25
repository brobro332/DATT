"use client";

import { useState } from "react";
import { Button } from "@/components/common/Button";
import { Input } from "@/components/common/Input";

type PlaceSearchFormProps = {
  onSearch?: (keyword: string) => void;
};

export function PlaceSearchForm({
  onSearch,
}: PlaceSearchFormProps) {
  const [keyword, setKeyword] = useState("");

  function handleSubmit(event: { preventDefault: () => void }) {
    event.preventDefault();

    onSearch?.(keyword);
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="flex flex-col gap-3 rounded-3xl border border-gray-200 bg-white p-4 shadow-sm md:flex-row"
    >
      <div className="flex-1">
        <Input
          id="keyword"
          type="text"
          placeholder="장소명, 지역, 카테고리를 검색해보세요"
          value={keyword}
          onChange={(event) => setKeyword(event.target.value)}
        />
      </div>

      <Button type="submit" className="md:w-28">
        검색
      </Button>
    </form>
  );
}