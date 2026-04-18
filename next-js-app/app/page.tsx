'use client';

import { useState, useEffect, useRef, useCallback, useMemo } from 'react';
import { Search, Sun, Moon, Anchor, Loader2, ArrowUp } from 'lucide-react';
import { useSearch } from '@/hooks/useSearch';
import { PlaceCard } from '@/components/PlaceCard';
import { AnchorModal } from '@/components/AnchorModal';

export default function Home() {
  const [keyword, setKeyword] = useState('');
  const [tempKeyword, setTempKeyword] = useState('');
  const [activeTab, setActiveTab] = useState<'NAVER' | 'GOOGLE'>('NAVER');
  const [activeCategory, setActiveCategory] = useState('전체');
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [showTopBtn, setShowTopBtn] = useState(false); // ✅ 버튼 상태
  
  const [anchorLoading, setAnchorLoading] = useState(false);
  const [anchorData, setAnchorData] = useState(null);
  const [isAnchorModalOpen, setIsAnchorModalOpen] = useState(false);

  const { results, totalElements, loading, fetchingNext, hasNext, setPage } = 
    useSearch(keyword, activeTab, activeCategory, 'REVIEW');

  const isAnchorDisabled = useMemo(() => {
    return anchorLoading || !keyword || tempKeyword !== keyword;
    }, [anchorLoading, keyword, tempKeyword]);

  useEffect(() => {
    if (isDarkMode) document.documentElement.classList.add('dark');
    else document.documentElement.classList.remove('dark');

    const handleScroll = () => {
      if (window.scrollY > 300) setShowTopBtn(true);
      else setShowTopBtn(false);
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [isDarkMode]);

  const observer = useRef<IntersectionObserver | null>(null);
  const lastElementRef = useCallback((node: HTMLDivElement) => {
    if (loading || fetchingNext) return;
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasNext) {
        setPage(p => p + 1);
      }
    });
    if (node) observer.current.observe(node);
  }, [loading, fetchingNext, hasNext, setPage]);

  const handleCreateAnchor = async () => {
    if (isAnchorDisabled) return;

    setAnchorLoading(true);
    try {
      const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/anchors?keyword=${encodeURIComponent(keyword || '전체')}`, { method: 'POST' });
      const result = await res.json();
      if (result.success) {
        setAnchorData(result.data);
        setIsAnchorModalOpen(true);
      }
    } catch (e) {
      alert('데이터 생성 오류');
    } finally {
      setAnchorLoading(false);
    }
  };

  const categories = useMemo(() => 
    activeTab === 'GOOGLE' ? ['전체', '맛집', '카페', '술집'] : ['전체', '맛집', '카페', '숙소', '술집'], 
  [activeTab]);

  return (
    <div className="min-h-screen bg-[#F8FAFC] dark:bg-[#020617] transition-colors font-sans pb-20">
      <header className="sticky top-0 z-[60] bg-white/80 dark:bg-slate-950/80 backdrop-blur-xl border-b border-slate-200 dark:border-white/5">
        <div className="max-w-2xl mx-auto px-6 py-5">
          <div className="flex items-center justify-between mb-6">
            <h1 className="text-2xl font-black italic">DATT<span className="text-indigo-600">.</span></h1>
            <div className="flex items-center gap-3">
              <button onClick={() => setIsDarkMode(!isDarkMode)} className="p-2.5 rounded-xl bg-slate-100 dark:bg-slate-900 text-slate-500">
                {isDarkMode ? <Sun size={18} /> : <Moon size={18} />}
              </button>
              <div className="flex p-1 bg-slate-100 dark:bg-slate-900 rounded-xl font-black text-[10px]">
                {['NAVER', 'GOOGLE'].map(t => (
                  <button key={t} onClick={() => setActiveTab(t as any)} className={`px-4 py-1.5 rounded-lg transition-all ${activeTab === t ? 'bg-white dark:bg-slate-800 text-indigo-600 shadow-sm' : 'text-slate-400'}`}>
                    {t}
                  </button>
                ))}
              </div>
            </div>
          </div>
          <div className="flex gap-2">
            <div className="relative flex-1 group">
              <input 
                type="text" 
                className="w-full pl-12 pr-4 py-4 bg-slate-100 dark:bg-slate-900 rounded-2xl outline-none focus:ring-2 ring-indigo-500/20 dark:text-white" 
                placeholder="검색어를 입력하세요"
                value={tempKeyword}
                onChange={(e) => setTempKeyword(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && setKeyword(tempKeyword)}
              />
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={20} />
            </div>
            <button 
              onClick={handleCreateAnchor} 
              disabled={isAnchorDisabled} 
              className={`px-6 rounded-2xl transition-all shadow-lg active:scale-95 flex items-center justify-center ${
                isAnchorDisabled 
                  ? 'bg-slate-200 dark:bg-slate-800 text-slate-400 cursor-not-allowed' 
                  : 'bg-indigo-600 text-white hover:bg-indigo-700 shadow-indigo-500/20'
              }`}
            >
              {anchorLoading ? <Loader2 size={20} className="animate-spin" /> : <Anchor size={20} />}
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-2xl mx-auto px-6 py-8">
      {/* 총 건수 표시 추가 */}
      <div className="flex items-center justify-between mb-4 px-1">
        <p className="text-sm font-medium text-slate-500">
          {loading ? (
            <span className="flex items-center gap-2">
              <div className="w-16 h-4 bg-slate-200 dark:bg-slate-800 animate-pulse rounded" />
              데이터를 찾는 중...
            </span>
          ) : (
            <>총 <span className="text-indigo-600 font-bold">{totalElements.toLocaleString()}</span>개의 장소를 찾았습니다</>
          )}
        </p>
      </div>

      <div className="flex gap-2 overflow-x-auto no-scrollbar mb-8">
        {categories.map(cat => (
          <button 
            key={cat} 
            onClick={() => setActiveCategory(cat)} 
            className={`px-6 py-2.5 rounded-full text-xs font-black transition-all whitespace-nowrap active:scale-95 ${
              activeCategory === cat 
                ? 'bg-slate-900 dark:bg-white text-white dark:text-slate-900 shadow-lg' 
                : 'bg-white dark:bg-slate-900 text-slate-500 border border-slate-200 dark:border-white/5 hover:border-indigo-500/50'
            }`}
          >
            {cat}
          </button>
        ))}
      </div>
      
      <div className="space-y-5">
        {/* 1. 초기 로딩 시 스켈레톤 5개 출력 */}
        {loading && results.length === 0 && (
          <>
            {[...Array(5)].map((_, i) => (
              <div key={i} className="w-full h-32 bg-white dark:bg-slate-900 rounded-2xl animate-pulse border border-slate-100 dark:border-white/5 flex p-4 gap-4">
                <div className="w-24 h-24 bg-slate-200 dark:bg-slate-800 rounded-xl" />
                <div className="flex-1 space-y-3 py-1">
                  <div className="w-1/3 h-5 bg-slate-200 dark:bg-slate-800 rounded" />
                  <div className="w-full h-4 bg-slate-100 dark:bg-slate-800/50 rounded" />
                  <div className="w-2/3 h-4 bg-slate-100 dark:bg-slate-800/50 rounded" />
                </div>
              </div>
            ))}
          </>
        )}

        {!loading && results.length === 0 && (
          <div className="py-20 text-center">
            <div className="inline-flex p-5 rounded-full bg-slate-100 dark:bg-slate-900 text-slate-400 mb-4">
              <Search size={32} />
            </div>
            <p className="text-slate-500 font-medium">검색 결과가 없습니다.</p>
            <p className="text-sm text-slate-400 mt-1">다른 키워드로 검색해 보시겠어요?</p>
          </div>
        )}

        {/* 2. 실제 결과 리스트 */}
        {results.map((place, i) => (
          <PlaceCard 
            key={`${place.name}-${i}`} 
            place={place} 
            ref={i === results.length - 1 ? lastElementRef : null} 
          />
        ))}
        
        {/* 3. 추가 데이터 페칭(무한 스크롤) 시 하단 스켈레톤 1개 */}
        {fetchingNext && (
          <div className="w-full h-32 bg-white dark:bg-slate-900 rounded-2xl animate-pulse border border-slate-100 dark:border-white/5 flex p-4 gap-4">
            <div className="w-24 h-24 bg-slate-200 dark:bg-slate-800 rounded-xl" />
            <div className="flex-1 space-y-3 py-1">
              <div className="w-1/4 h-5 bg-slate-200 dark:bg-slate-800 rounded" />
              <div className="w-full h-4 bg-slate-100 dark:bg-slate-800/50 rounded" />
            </div>
          </div>
        )}
      </div>
    </main>

      {showTopBtn && (
        <button 
          onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })} 
          className="fixed bottom-8 right-8 z-[70] p-4 bg-indigo-600 text-white rounded-2xl shadow-[0_20px_40px_-10px_rgba(79,70,229,0.5)] active:scale-90 transition-all animate-in fade-in slide-in-from-bottom-4"
        >
          <ArrowUp size={24} strokeWidth={3} />
        </button>
      )}

      <AnchorModal isOpen={isAnchorModalOpen} onClose={() => setIsAnchorModalOpen(false)} anchorData={anchorData} keyword={keyword} />
    </div>
  );
}