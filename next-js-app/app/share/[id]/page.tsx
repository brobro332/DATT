'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { ArrowLeft, Crown, Loader2, Sparkles } from 'lucide-react';
import { PlaceCard } from '@/components/PlaceCard';

export default function SharePage() {
  const { id } = useParams();
  const router = useRouter();
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  
  const [activeTab, setActiveTab] = useState<'NAVER' | 'GOOGLE'>('NAVER');
  const [selectedCategory, setSelectedCategory] = useState('맛집');

  useEffect(() => {
    const fetchSharedList = async () => {
      try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/anchors/${id}`);
        const result = await res.json();
        if (result.success) {
          setData(result.data);
          const firstCat = Object.keys(result.data.content?.NAVER || {})[0];
          if (firstCat) setSelectedCategory(firstCat);
        }
      } catch (e) {
        console.error('Data load error:', e);
      } finally {
        setLoading(false);
      }
    };
    if (id) fetchSharedList();
  }, [id]);

  if (loading) return (
    <div className="min-h-screen flex items-center justify-center bg-white dark:bg-[#020617]">
      <Loader2 className="animate-spin text-indigo-600" size={40} />
    </div>
  );

  const currentSourceData = data.content?.[activeTab] || {};
  const categories = Object.keys(currentSourceData);

  return (
    <div className="min-h-screen bg-white dark:bg-[#020617] pb-24 font-sans transition-colors">
      
      {/* 1. 히어로 섹션: 수직 보라색 그라데이션 적용 ✅ */}
      <section className="relative bg-gradient-to-b from-slate-950 via-indigo-950 to-indigo-900 text-white pt-6 pb-20 px-6 rounded-b-[48px] shadow-2xl overflow-hidden">
        {/* 은은한 빛 효과 추가 (상용 퀄리티) */}
        <div className="absolute top-0 right-0 w-64 h-64 bg-indigo-500/20 blur-[100px] -mr-32 -mt-32 rounded-full" />
        
        <div className="max-w-2xl mx-auto relative z-10">
          <header className="flex items-center justify-between mb-12">
            <button onClick={() => router.push('/')} className="p-2 -ml-2 hover:bg-white/10 rounded-xl transition-colors">
              <ArrowLeft size={24} />
            </button>
            <h1 className="text-xl font-[1000] italic leading-none tracking-tighter">DATT<span className="text-indigo-400">.</span></h1>
            <div className="w-10" />
          </header>

          <div className="space-y-5">
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-white/10 border border-white/10 text-indigo-200">
              <Sparkles size={14} className="animate-pulse" />
              <span className="text-[10px] font-black uppercase tracking-[0.2em]">Curation for you</span>
            </div>
            <h2 className="text-4xl font-[1000] leading-[1.1] tracking-tight">
              추천 키워드<br />
              <span className="bg-clip-text text-transparent bg-gradient-to-r from-indigo-300 to-purple-200">
                {data.keyword}
              </span>
            </h2>
            <p className="text-indigo-200/60 font-bold text-sm leading-relaxed max-w-[280px]">
              리뷰와 평점 데이터를 분석하여 도출한<br />가장 신뢰도 높은 장소들입니다.
            </p>
          </div>
        </div>
      </section>

      <main className="max-w-2xl mx-auto px-6 -mt-10 relative z-20">
        {/* 2. 컨트롤러 섹션 */}
        <div className="bg-white dark:bg-slate-900 rounded-[32px] p-2.5 shadow-[0_20px_50px_rgba(0,0,0,0.1)] border border-slate-100 dark:border-white/5 mb-10">
          <div className="flex p-1 bg-slate-100 dark:bg-slate-800 rounded-[22px] mb-2.5">
            {['NAVER', 'GOOGLE'].map(t => (
              <button 
                key={t} 
                onClick={() => {
                  setActiveTab(t as any);
                  const firstCat = Object.keys(data.content?.[t] || {})[0];
                  if (firstCat) setSelectedCategory(firstCat);
                }} 
                className={`flex-1 py-3 rounded-[18px] text-xs font-black transition-all ${
                  activeTab === t 
                  ? 'bg-white dark:bg-slate-700 text-indigo-600 shadow-sm scale-[1.02]' 
                  : 'text-slate-400 hover:text-slate-500'
                }`}
              >
                {t}
              </button>
            ))}
          </div>

          <div className="flex gap-1.5 overflow-x-auto no-scrollbar p-1">
            {categories.map(cat => (
              <button 
                key={cat} 
                onClick={() => setSelectedCategory(cat)} 
                className={`px-5 py-2.5 rounded-[16px] text-[13px] font-bold transition-all whitespace-nowrap ${
                  selectedCategory === cat 
                  ? 'text-indigo-600 bg-indigo-50 dark:bg-indigo-500/10' 
                  : 'text-slate-400 hover:text-slate-500'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        {/* 3. 리스트 영역 */}
        <div className="space-y-6">
          <div className="space-y-5">
            {currentSourceData[selectedCategory]?.map((place: any, idx: number) => {
              const rank = idx + 1;
              return (
                <div key={idx} className="relative group transition-all duration-300">
                  <div className={`absolute -left-3 -top-3 z-10 w-9 h-9 rounded-2xl flex items-center justify-center font-black shadow-lg transform -rotate-12 group-hover:rotate-0 transition-transform ${
                    rank === 1 ? 'bg-amber-400 text-amber-900' : 
                    rank === 2 ? 'bg-slate-300 text-slate-700' :
                    rank === 3 ? 'bg-orange-300 text-orange-900' :
                    'bg-white dark:bg-slate-800 text-slate-400 border border-slate-100 dark:border-white/5'
                  }`}>
                    {rank === 1 ? <Crown size={18} strokeWidth={2.5} /> : rank}
                  </div>
                  <PlaceCard place={place} />
                </div>
              );
            })}
          </div>
        </div>

        <div className="mt-20 border-t border-slate-100 dark:border-white/5 pt-12 pb-8 text-center">
          <p className="text-xs font-black text-slate-300 uppercase tracking-[0.3em] mb-4">Powered by DATT.</p>
          <button 
            onClick={() => router.push('/')}
            className="px-10 py-4.5 bg-indigo-600 dark:bg-indigo-500 text-white rounded-2xl font-black text-sm shadow-xl shadow-indigo-500/20 active:scale-95 transition-all"
          >
            나만의 리스트 만들기
          </button>
        </div>
      </main>

      <style jsx global>{`
        .no-scrollbar::-webkit-scrollbar { display: none; }
        .no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
      `}</style>
    </div>
  );
}