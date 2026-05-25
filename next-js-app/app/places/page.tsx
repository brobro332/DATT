"use client";

import { useState } from "react";

import { MainLayout } from "@/layouts/MainLayout";
import { SectionTitle } from "@/components/common/SectionTitle";
import { Button } from "@/components/common/Button";
import { AsyncStateView } from "@/components/common/AsyncStateView";
import { PlaceSearchForm } from "@/components/place/PlaceSearchForm";
import { PlaceFilterChips } from "@/components/place/PlaceFilterChips";
import { PlaceListItem } from "@/components/place/PlaceListItem";
import { usePlaceSearch } from "@/hooks/usePlaceSearch";

const PAGE_SIZE = 10;

export default function PlacesPage() {
  const [keyword, setKeyword] = useState("");
  const [page, setPage] = useState(0);

  const { data, isLoading, isError } = usePlaceSearch({
    keyword,
    page,
    size: PAGE_SIZE,
  });

  function handleSearch(nextKeyword: string) {
    setKeyword(nextKeyword.trim());
    setPage(0);
  }

  function handlePreviousPage() {
    setPage((currentPage) => Math.max(currentPage - 1, 0));
  }

  function handleNextPage() {
    if (!data || data.last) {
      return;
    }

    setPage((currentPage) => currentPage + 1);
  }

  return (
    <MainLayout>
      <section className="space-y-6">
        <div className="rounded-3xl bg-gray-50 p-8">
          <SectionTitle
            eyebrow="Place Search"
            title="어디로 떠나볼까요?"
            description="장소명, 지역, 업종을 기준으로 DATT의 장소 데이터를 탐색해보세요."
          />

          <PlaceSearchForm onSearch={handleSearch} />

          <div className="mt-4">
            <PlaceFilterChips />
          </div>
        </div>

        <section>
          <div className="mb-4 flex items-center justify-between">
            <h2 className="text-xl font-bold text-gray-950">
              검색 결과
            </h2>

            <p className="text-sm text-gray-500">
              {data
                ? `${data.totalElements.toLocaleString()}개 검색됨`
                : "검색어를 입력해주세요."}
            </p>
          </div>

          <AsyncStateView
            isLoading={isLoading}
            isError={isError}
            isEmpty={Boolean(data?.empty)}
            loadingMessage="장소를 검색하는 중입니다..."
            errorTitle="장소 검색 실패"
            errorMessage="장소 검색 중 문제가 발생했습니다."
            emptyTitle="검색 결과가 없습니다."
            emptyDescription="다른 키워드로 다시 검색해보세요."
          >
            {data && (
              <>
                <ul className="overflow-hidden rounded-3xl border border-gray-200 bg-white">
                  {data.content.map((place) => (
                    <PlaceListItem key={place.id} place={place} />
                  ))}
                </ul>

                <div className="mt-5 flex items-center justify-between">
                  <p className="text-sm text-gray-500">
                    {data.number + 1} / {data.totalPages} 페이지
                  </p>

                  <div className="flex gap-2">
                    <Button
                      type="button"
                      variant="secondary"
                      onClick={handlePreviousPage}
                      disabled={data.first}
                    >
                      이전
                    </Button>

                    <Button
                      type="button"
                      variant="secondary"
                      onClick={handleNextPage}
                      disabled={data.last}
                    >
                      다음
                    </Button>
                  </div>
                </div>
              </>
            )}
          </AsyncStateView>
        </section>
      </section>
    </MainLayout>
  );
}