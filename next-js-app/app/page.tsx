'use client';

import { useState, useEffect, useRef, useCallback, useMemo } from 'react';
import { Search, MapPin, Star, ArrowUp, TrendingUp, Loader2, Image as ImageIcon, Sun, Moon, Sparkles, Heart } from 'lucide-react';

export default function Home() {
  const [keyword, setKeyword] = useState('');
  const [activeTab, setActiveTab] = useState<'NAVER' | 'GOOGLE'>('NAVER');
  const [activeCategory, setActiveCategory] = useState('전체');
  const [results, setResults] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [fetchingNext, setFetchingNext] = useState(false);
  const [page, setPage] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [sortBy, setSortBy] = useState<'REVIEW' | 'RATING'>('REVIEW');
  const [showTopBtn, setShowTopBtn] = useState(false);
  const [totalElements, setTotalElements] = useState(0);
  const [isDarkMode, setIsDarkMode] = useState(false);

  const [hoveredRating, setHoveredRating] = useState<Record<string, number>>({});
  const [userRatings, setUserRatings] = useState<Record<string, number>>({});

  const observer = useRef<IntersectionObserver | null>(null);

  useEffect(() => {
    if (isDarkMode) document.documentElement.classList.add('dark');
    else document.documentElement.classList.remove('dark');
  }, [isDarkMode]);

  useEffect(() => { setActiveCategory('전체'); }, [activeTab]);

  const filteredCategories = useMemo(() => {
    const base = ['전체', '맛집', '카페', '숙소', '술집'];
    return activeTab === 'GOOGLE' ? base.filter(c => c !== '숙소') : base;
  }, [activeTab]);

  const lastElementRef = useCallback((node: HTMLDivElement) => {
    if (loading || fetchingNext) return;
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasNext) setPage(prev => prev + 1);
    });
    if (node) observer.current.observe(node);
  }, [loading, fetchingNext, hasNext]);

  useEffect(() => {
    const handleScroll = () => setShowTopBtn(window.scrollY > 400);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  useEffect(() => { handleSearch(true); }, [activeTab, activeCategory, sortBy]);
  useEffect(() => { if (page > 0) handleSearch(false); }, [page]);

  const handleSearch = useCallback(async (isNew = true) => {
    if (isNew) {
      setLoading(true);
      // 중요: 새 검색/필터 시 기존 결과와 페이지를 즉시 초기화
      setResults([]); 
      setPage(0);
    } else {
      setFetchingNext(true);
    }

    try {
      // isNew일 때는 무조건 0페이지, 아닐 때는 현재 page 상태 사용
      const currentPage = isNew ? 0 : page;
      const categoryParam = activeCategory === '전체' ? '' : activeCategory;
      
      // API 호출 시 keyword가 없어도 동작하도록 처리 (백엔드 사양에 맞춤)
      const res = await fetch(
        `http://localhost:8080/api/v1/places?keyword=${encodeURIComponent(keyword)}&category=${encodeURIComponent(categoryParam)}&platform=${activeTab}&sortBy=${sortBy}&page=${currentPage}&size=20`
      );
      
      const result = await res.json();
      
      if (result.success) {
        const { content, last, totalElements: total } = result.data;
        setResults(prev => isNew ? content : [...prev, ...content]);
        setHasNext(!last);
        setTotalElements(total);
      }
    } catch (error) {
      console.error('Data Load Failed:', error);
    } finally {
      setLoading(false);
      setFetchingNext(false);
    }
  }, [keyword, activeCategory, activeTab, sortBy, page]);

  const getRatingText = (score: number) => {
    const texts: Record<number, string> = {
      5: '최고예요!',
      4: '좋아요',
      3: '보통이에요',
      2: '별로예요',
      1: '최악이에요'
    };
    return texts[score] || '평가하기';
  };

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-[#020617] text-slate-900 dark:text-slate-100 transition-colors duration-300 font-sans selection:bg-indigo-500 selection:text-white">
      
      {/* HEADER */}
      <header className="sticky top-0 z-50 bg-white/90 dark:bg-slate-950/90 backdrop-blur-2xl border-b-2 border-slate-200 dark:border-slate-800">
        <div className="max-w-2xl mx-auto px-6 py-5">
          <div className="flex items-center justify-between mb-6">
            <h1 className="text-3xl font-[1000] tracking-tighter italic text-slate-900 dark:text-white underline decoration-indigo-600 decoration-4 underline-offset-4">DATT<span className="text-indigo-600">.</span></h1>
            
            <div className="flex items-center gap-4">
              <button 
                onClick={() => setIsDarkMode(!isDarkMode)}
                className="p-2.5 rounded-2xl bg-slate-100 dark:bg-slate-900 text-slate-600 dark:text-slate-400 hover:scale-110 transition-all border border-transparent hover:border-slate-200"
              >
                {isDarkMode ? <Sun size={20} /> : <Moon size={20} />}
              </button>

              <div className="flex p-1 bg-slate-100 dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800">
                {['NAVER', 'GOOGLE'].map((platform) => (
                  <button
                    key={platform}
                    onClick={() => setActiveTab(platform as any)}
                    className={`px-6 py-2 rounded-xl text-[11px] font-black tracking-widest transition-all ${
                      activeTab === platform 
                        ? 'bg-white dark:bg-slate-800 text-indigo-600 dark:text-indigo-400 shadow-xl' 
                        : 'text-slate-400 hover:text-slate-600'
                    }`}
                  >
                    {platform}
                  </button>
                ))}
              </div>
            </div>
          </div>
          
          <div className="relative group">
            <input
              type="text"
              className="w-full pl-14 pr-6 py-4.5 bg-slate-100 dark:bg-slate-900 border-2 border-transparent rounded-[20px] outline-none transition-all focus:bg-white dark:focus:bg-slate-800 focus:border-indigo-600 focus:ring-8 focus:ring-indigo-600/5 text-lg font-bold placeholder:text-slate-400 dark:text-white shadow-inner"
              placeholder="장소를 검색해보세요"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch(true)}
            />
            <Search className="absolute left-5 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-indigo-600 transition-colors" size={24} />
          </div>
        </div>
      </header>

      <main className="max-w-2xl mx-auto px-6 py-10">
        {/* FILTERS */}
        <div className="space-y-8 mb-12">
          <div className="flex gap-3 overflow-x-auto no-scrollbar pb-2">
            {filteredCategories.map((cat) => (
              <button
                key={cat}
                onClick={() => setActiveCategory(cat)}
                className={`px-8 py-3 rounded-2xl text-sm font-black whitespace-nowrap transition-all border-2 ${
                  activeCategory === cat 
                    ? 'bg-slate-900 dark:bg-indigo-600 text-white border-slate-900 dark:border-indigo-600 shadow-2xl shadow-indigo-500/20' 
                    : 'bg-white dark:bg-slate-900 text-slate-500 border-slate-200 dark:border-slate-800 hover:border-slate-300'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>

          {/* STATUS BAR WITH ANIMATION */}
          <div className="flex items-center justify-between border-b-2 border-slate-100 dark:border-slate-900 pb-4">
            <div className="flex items-center gap-2 h-6">
              {loading ? (
                <div className="flex items-center gap-2">
                  <div className="flex gap-1">
                    <span className="w-1.5 h-1.5 bg-indigo-500 rounded-full animate-bounce [animation-delay:-0.3s]"></span>
                    <span className="w-1.5 h-1.5 bg-indigo-500 rounded-full animate-bounce [animation-delay:-0.15s]"></span>
                    <span className="w-1.5 h-1.5 bg-indigo-500 rounded-full animate-bounce"></span>
                  </div>
                  <span className="text-[10px] font-black text-indigo-500 uppercase tracking-[0.2em] animate-pulse">Searching Places...</span>
                </div>
              ) : (
                <>
                  <Sparkles size={16} className="text-indigo-500" />
                  <span className="text-xs font-black text-slate-400 uppercase tracking-[0.2em]">{totalElements.toLocaleString()} PLACES FOUND</span>
                </>
              )}
            </div>
            <div className="flex gap-6">
              {[{ id: 'REVIEW', label: '인기순' }, { id: 'RATING', label: '평점순' }].map((filter) => (
                <button
                  key={filter.id}
                  onClick={() => setSortBy(filter.id as any)}
                  className={`text-xs font-black transition-all ${sortBy === filter.id ? 'text-indigo-600 dark:text-indigo-400' : 'text-slate-300 dark:text-slate-700 hover:text-slate-500'}`}
                >
                  {filter.label}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* CONTENT AREA */}
        <div className="space-y-6">
          {loading ? (
            /* ENHANCED SKELETON LOADING */
            [1, 2, 3, 4].map((n) => (
              <div key={`skeleton-${n}`} className="bg-white dark:bg-slate-900 p-6 rounded-[32px] border-2 border-slate-100 dark:border-slate-800 flex gap-6 animate-pulse">
                <div className="w-32 h-32 shrink-0 rounded-[24px] bg-slate-100 dark:bg-slate-800" />
                <div className="flex-1 space-y-4">
                  <div className="flex justify-between">
                    <div className="h-6 w-1/2 bg-slate-100 dark:bg-slate-800 rounded-lg" />
                    <div className="h-5 w-12 bg-slate-100 dark:bg-slate-800 rounded-md" />
                  </div>
                  <div className="h-4 w-3/4 bg-slate-50 dark:bg-slate-800/50 rounded-md" />
                  <div className="flex justify-between items-end pt-2">
                    <div className="flex gap-6">
                      <div className="space-y-2">
                        <div className="h-2 w-10 bg-slate-100 dark:bg-slate-800 rounded" />
                        <div className="h-4 w-8 bg-slate-100 dark:bg-slate-800 rounded" />
                      </div>
                      <div className="space-y-2">
                        <div className="h-2 w-10 bg-slate-100 dark:bg-slate-800 rounded" />
                        <div className="h-4 w-8 bg-slate-100 dark:bg-slate-800 rounded" />
                      </div>
                    </div>
                    <div className="h-8 w-24 bg-slate-50 dark:bg-slate-800/50 rounded-xl" />
                  </div>
                </div>
              </div>
            ))
          ) : results.length > 0 ? (
            <>
              {results.map((place, i) => (
                <div 
                  key={`${place.name}-${i}`} 
                  ref={i === results.length - 1 ? lastElementRef : null}
                  className="group bg-white dark:bg-slate-900 p-6 rounded-[32px] border-2 border-slate-100 dark:border-slate-800 shadow-sm hover:shadow-2xl hover:border-indigo-100 dark:hover:border-indigo-900 transition-all duration-300"
                >
                  <div className="flex gap-6">
                    {/* PLACE IMAGE */}
                    <div className="relative w-32 h-32 shrink-0 overflow-hidden rounded-[24px] bg-slate-50 dark:bg-slate-950">
                      {place.imageUrls?.[0] ? (
                        <img src={place.imageUrls[0]} className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110" alt={place.name} />
                      ) : (
                        <div className="w-full h-full flex items-center justify-center text-slate-200 dark:text-slate-800"><ImageIcon size={32} /></div>
                      )}
                      <div className="absolute top-3 left-3 px-3 py-1 bg-white/90 dark:bg-black/90 backdrop-blur shadow-sm rounded-xl text-[10px] text-slate-900 dark:text-white font-black">{place.category}</div>
                    </div>

                    {/* PLACE INFO */}
                    <div className="flex-1 min-w-0 flex flex-col justify-between">
                      <div>
                        <div className="flex items-start justify-between gap-3 mb-2">
                          <h3 className="text-xl font-black text-slate-900 dark:text-white truncate group-hover:text-indigo-600 transition-colors">{place.name}</h3>
                          <span className={`shrink-0 px-2.5 py-1 rounded-lg text-[10px] font-black ${place.platform === 'NAVER' ? 'bg-emerald-50 text-emerald-600 dark:bg-emerald-950 dark:text-emerald-400' : 'bg-blue-50 text-blue-600 dark:bg-blue-950 dark:text-blue-400'}`}>
                            {place.platform}
                          </span>
                        </div>

                        <div className="flex items-center gap-1.5 text-slate-400 dark:text-slate-500 text-sm font-bold">
                          <MapPin size={14} className="shrink-0 text-indigo-500" />
                          <span className="truncate">{place.address}</span>
                        </div>
                      </div>

                      <div className="flex items-end justify-between mt-4">
                        <div className="flex gap-8">
                          <div className="flex flex-col">
                            <span className="text-[9px] text-slate-400 font-black uppercase tracking-widest mb-0.5">Reviews</span>
                            <span className="text-sm font-black text-slate-900 dark:text-slate-200">{(place.visitorReviewCount || 0).toLocaleString()}</span>
                          </div>
                          <div className="flex flex-col">
                            <span className="text-[9px] text-slate-400 font-black uppercase tracking-widest mb-0.5">Rating</span>
                            <div className="flex items-center gap-1">
                              <Star size={12} className="text-amber-400 fill-amber-400" />
                              <span className="text-sm font-black text-slate-900 dark:text-slate-200">{place.rating?.toFixed(1) || '0.0'}</span>
                            </div>
                          </div>
                        </div>

                        {/* HIGH-END RATING UI (BACKGROUND REMOVED) */}
                        <div className="flex flex-col items-end gap-1.5 group/rating">
                          <span className={`text-[10px] font-black transition-all duration-300 ${
                            (hoveredRating[place.name] || userRatings[place.name]) 
                              ? 'text-indigo-600 dark:text-indigo-400 opacity-100 translate-y-0' 
                              : 'text-slate-300 dark:text-slate-700 opacity-0 translate-y-1'
                          }`}>
                            {getRatingText(hoveredRating[place.name] || userRatings[place.name] || 0)}
                          </span>

                          <div className="flex items-center gap-0.5">
                            {[1, 2, 3, 4, 5].map((star) => (
                              <button
                                key={star}
                                onMouseEnter={() => setHoveredRating({ ...hoveredRating, [place.name]: star })}
                                onMouseLeave={() => setHoveredRating({ ...hoveredRating, [place.name]: 0 })}
                                onClick={() => setUserRatings({ ...userRatings, [place.name]: star })}
                                className="relative p-0.5 transition-all duration-200 hover:scale-125 active:scale-95"
                              >
                                <Star 
                                  size={22} 
                                  strokeWidth={2.5}
                                  className={`transition-all duration-200 ${
                                    (hoveredRating[place.name] || userRatings[place.name] || 0) >= star 
                                      ? 'text-amber-400 fill-amber-400 drop-shadow-[0_0_8px_rgba(251,191,36,0.3)]' 
                                      : 'text-slate-200 dark:text-slate-800 hover:text-slate-300 dark:hover:text-slate-700'
                                  }`} 
                                />
                                {userRatings[place.name] === star && (
                                  <div className="absolute -bottom-1 left-1/2 -translate-x-1/2 w-1 h-1 bg-indigo-500 rounded-full animate-ping" />
                                )}
                              </button>
                            ))}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
              
              {/* FETCHING NEXT SKELETON */}
              {fetchingNext && (
                <div className="space-y-6 pt-4">
                  <div className="bg-white/50 dark:bg-slate-900/50 p-6 rounded-[32px] border-2 border-dashed border-slate-100 dark:border-slate-800 flex gap-6 animate-pulse">
                    <div className="w-32 h-32 shrink-0 rounded-[24px] bg-slate-100/50 dark:bg-slate-800/50" />
                    <div className="flex-1 space-y-4">
                      <div className="h-6 w-1/3 bg-slate-100/50 dark:bg-slate-800/50 rounded-lg" />
                      <div className="h-4 w-1/2 bg-slate-50/50 dark:bg-slate-800/30 rounded-md" />
                    </div>
                  </div>
                  <div className="flex justify-center py-6">
                    <div className="flex items-center gap-3 px-6 py-3 bg-white dark:bg-slate-900 rounded-2xl shadow-xl border border-slate-100 dark:border-slate-800">
                      <Loader2 className="animate-spin text-indigo-600" size={20} />
                      <span className="text-[11px] font-black text-slate-500 uppercase tracking-widest">Loading Content</span>
                    </div>
                  </div>
                </div>
              )}
            </>
          ) : (
            <div className="text-center py-32 bg-white dark:bg-slate-900 rounded-[40px] border-2 border-dashed border-slate-200 dark:border-slate-800">
              <p className="text-slate-400 font-black text-lg">검색 결과가 없습니다.</p>
            </div>
          )}
        </div>
      </main>

      {/* SCROLL TO TOP */}
      <button
        onClick={scrollToTop}
        className={`fixed bottom-10 right-10 p-5 bg-slate-900 dark:bg-indigo-600 text-white rounded-[24px] shadow-2xl transition-all duration-500 hover:scale-110 active:scale-95 z-50 ${showTopBtn ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-20 pointer-events-none'}`}
      >
        <ArrowUp size={24} strokeWidth={3} />
      </button>
    </div>
  );
}