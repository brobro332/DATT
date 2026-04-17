'use client';

import { useState, useCallback, useEffect } from 'react';

export function useSearch(keyword: string, activeTab: string, activeCategory: string, sortBy: string) {
  const [results, setResults] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [fetchingNext, setFetchingNext] = useState(false);
  const [page, setPage] = useState(0);
  const [hasNext, setHasNext] = useState(false);

  const handleSearch = useCallback(async (isNew = true) => {
    // ⚠️ 핵심: 새로운 검색일 때만 리스트를 비웁니다. 추가 로딩 시에는 유지!
    if (isNew) {
      setLoading(true);
      setPage(0);
      // setResults([])를 여기서 안 하고 데이터가 온 직후에 갈아끼워야 깜빡임이 없습니다.
    } else {
      setFetchingNext(true);
    }

    try {
      const currentPage = isNew ? 0 : page;
      const categoryParam = activeCategory === '전체' ? '' : activeCategory;
      const res = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/places?keyword=${encodeURIComponent(keyword || '')}&category=${encodeURIComponent(categoryParam)}&platform=${activeTab}&sortBy=${sortBy}&page=${currentPage}&size=20`
      );
      const result = await res.json();
      
      if (result.success) {
        const { content, last } = result.data;
        // ⚠️ 여기서 isNew 여부에 따라 합치거나 새로 만듭니다.
        setResults(prev => isNew ? content : [...prev, ...content]);
        setHasNext(!last);
      }
    } catch (error) {
      console.error('검색 실패:', error);
    } finally {
      setLoading(false);
      setFetchingNext(false);
    }
  }, [keyword, activeCategory, activeTab, sortBy, page]);

  // 설정 변경 시 새 검색
  useEffect(() => {
    handleSearch(true);
  }, [keyword, activeTab, activeCategory, sortBy]);

  // 페이지 변경 시 추가 로딩
  useEffect(() => {
    if (page > 0) handleSearch(false);
  }, [page]);

  return { results, loading, fetchingNext, hasNext, setPage };
}