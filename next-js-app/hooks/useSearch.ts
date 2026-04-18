'use client';

import { useState, useCallback, useEffect, useRef } from 'react';

export function useSearch(keyword: string, activeTab: string, activeCategory: string, sortBy: string) {
  const [results, setResults] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [fetchingNext, setFetchingNext] = useState(false);
  const [page, setPage] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [totalElements, setTotalElements] = useState(0);

  const isRequesting = useRef(false);

  const handleSearch = useCallback(async (isNew = true) => {
    if (isRequesting.current) return;
    
    if (isNew) {
      setLoading(true);
      setPage(0);
      // setResults([])를 여기서 안 하고 데이터가 온 직후에 갈아끼워야 깜빡임이 없습니다.
    } else {
      setFetchingNext(true);
    }

    isRequesting.current = true;

    try {
      const currentPage = isNew ? 0 : page;
      const categoryParam = activeCategory === '전체' ? '' : activeCategory;
      const res = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/places?keyword=${encodeURIComponent(keyword || '')}&category=${encodeURIComponent(categoryParam)}&platform=${activeTab}&sortBy=${sortBy}&page=${currentPage}&size=20`
      );
      const result = await res.json();
      
      if (result.success) {
        const { content, last } = result.data;
        setResults(prev => isNew ? content : [...prev, ...content]);
        setTotalElements(result.data.totalElements);
        setHasNext(!last);
      }
    } catch (error) {
      console.error('검색 실패:', error);
    } finally {
      setLoading(false);
      setFetchingNext(false);
      isRequesting.current = false;
    }
  }, [keyword, activeCategory, activeTab, sortBy, page]);

  useEffect(() => {
    handleSearch(true);
  }, [keyword, activeTab, activeCategory, sortBy]);

  useEffect(() => {
    if (page > 0) handleSearch(false);
  }, [page]);

  return { results, totalElements, loading, fetchingNext, hasNext, setPage };
}