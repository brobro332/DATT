'use client';

import { useState } from 'react';
import { X, Anchor, Check, Copy, Share2, Sparkles, MapPin } from 'lucide-react';

interface AnchorModalProps {
  isOpen: boolean;
  onClose: () => void;
  anchorData: any;
  keyword: string;
}

export const AnchorModal = ({ isOpen, onClose, anchorData, keyword }: AnchorModalProps) => {
  const [copySuccess, setCopySuccess] = useState(false);

  if (!isOpen || !anchorData) return null;

  const handleCopy = () => {
    if (typeof window !== 'undefined') {
      navigator.clipboard.writeText(anchorData.shareUrl);
      setCopySuccess(true);
      setTimeout(() => setCopySuccess(false), 2000);
    }
  };

  const handleNativeShare = async () => {
    if (navigator.share) {
      try {
        await navigator.share({
          title: `📍 ${keyword || '추천'} 장소 리스트`,
          text: `닻(DATT)이 엄선한 ${keyword} 로컬 리스트입니다.`,
          url: anchorData.shareUrl,
        });
      } catch (error) {
        console.error('공유 실패:', error);
      }
    } else {
      handleCopy();
    }
  };

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 sm:p-6">
      <div 
        className="absolute inset-0 bg-slate-950/80 backdrop-blur-xl animate-in fade-in duration-500" 
        onClick={onClose} 
      />
      
      <div className="relative bg-white dark:bg-[#0b1120] w-full max-w-[420px] rounded-[38px] shadow-[0_32px_64px_-12px_rgba(0,0,0,0.5)] overflow-hidden border border-white/10 flex flex-col max-h-[85vh] animate-in zoom-in-95 slide-in-from-bottom-4 duration-400">
        
        <div className="relative p-8 pb-10 overflow-hidden">
          <div className="absolute top-0 right-0 -mr-16 -mt-16 w-64 h-64 bg-indigo-600/10 rounded-full blur-3xl" />
          
          <button 
            onClick={onClose} 
            className="absolute top-4 right-4 p-4 text-slate-400 hover:text-slate-600 dark:hover:text-white transition-all active:scale-75 z-20 group"
            aria-label="Close modal"
          >
            <div className="p-2 bg-slate-100 dark:bg-white/10 rounded-full group-hover:bg-slate-200 dark:group-hover:bg-white/20">
              <X size={20} />
            </div>
          </button>

          <div className="relative z-10">
            <div className="inline-flex items-center gap-2 px-3 py-1 bg-indigo-50 dark:bg-indigo-500/10 text-indigo-600 dark:text-indigo-400 rounded-full text-[10px] font-black tracking-widest uppercase mb-4">
              <Sparkles size={12} /> Curated for you
            </div>
            
            <h2 className="text-3xl font-black leading-[1.2] text-slate-900 dark:text-white tracking-tight">
              당신을 위한<br />
              <span className="text-indigo-600">특별한 선택 ⚓️</span>
            </h2>
            <p className="text-slate-500 dark:text-slate-400 text-sm mt-3 font-medium leading-relaxed">
              가장 좋은 반응을 얻고 있는 <span className="text-slate-900 dark:text-white font-bold">"{keyword || '엄선된'}"</span> 베스트 장소를 한데 모았습니다.
            </p>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto px-6 space-y-3.5 no-scrollbar pb-4">
          {anchorData.topPlaces?.map((p: any, idx: number) => (
            <div 
              key={idx} 
              className="group flex items-center gap-4 p-4 rounded-[24px] bg-slate-50 dark:bg-white/5 border border-slate-100 dark:border-white/[0.08] hover:bg-white dark:hover:bg-white/[0.12] transition-all duration-300"
            >
              <div className="relative w-16 h-16 shrink-0 rounded-[18px] overflow-hidden bg-slate-200">
                <img src={p.imageUrl} className="w-full h-full object-cover" alt={p.name} />
              </div>
              
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-1.5 mb-1">
                  <span className="text-indigo-600 dark:text-indigo-400 text-xs font-black">{idx + 1}</span>
                  <p className="text-sm font-black text-slate-900 dark:text-white truncate tracking-tight">{p.name}</p>
                </div>
                <div className="flex items-center gap-1 text-slate-400">
                  <MapPin size={10} />
                  <p className="text-[11px] font-bold truncate">{p.address}</p>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="p-8 pt-4 bg-white dark:bg-[#0b1120] border-t border-slate-100 dark:border-white/5 space-y-4">
          <div className="flex items-center gap-2 p-1.5 bg-slate-100 dark:bg-slate-900/50 rounded-2xl border border-slate-200/50 dark:border-white/5">
            <div className="flex-1 px-3 text-[11px] font-bold text-slate-400 truncate tracking-tighter">
              {anchorData.shareUrl}
            </div>
            <button 
              onClick={handleCopy}
              className={`px-4 py-2.5 rounded-xl font-black text-[11px] transition-all flex items-center gap-1.5 ${
                copySuccess 
                ? 'bg-emerald-500 text-white' 
                : 'bg-white dark:bg-slate-800 text-slate-900 dark:text-white border border-slate-200 dark:border-white/10 shadow-sm'
              }`}
            >
              {copySuccess ? <Check size={14} /> : <Copy size={14} />}
              {copySuccess ? '복사됨' : '복사'}
            </button>
          </div>
          
          <button 
            onClick={handleNativeShare}
            className="w-full py-5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-[22px] font-black text-[15px] flex items-center justify-center gap-2.5 shadow-xl shadow-indigo-500/20 transition-all active:scale-[0.97]"
          >
            <Share2 size={18} /> 
            리스트 공유하기
          </button>
        </div>
      </div>
    </div>
  );
};