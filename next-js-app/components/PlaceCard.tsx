'use client';

import { forwardRef, useState } from 'react';
import { MapPin, Star, Image as ImageIcon, ExternalLink } from 'lucide-react';

interface PlaceCardProps {
  place: any;
}

export const PlaceCard = forwardRef<HTMLDivElement, PlaceCardProps>(({ place }, ref) => {
  const [hoveredRating, setHoveredRating] = useState(0);
  const [userRating, setUserRating] = useState(0);

  const getRatingText = (score: number) => {
    const texts: Record<number, string> = { 5: '최고예요!', 4: '좋아요', 3: '보통이에요', 2: '별로예요', 1: '최악이에요' };
    return texts[score] || '평가하기';
  };

  return (
    <div ref={ref} className="bg-white dark:bg-slate-900 p-5 rounded-[28px] border border-slate-200 dark:border-white/5 hover:shadow-2xl hover:shadow-indigo-500/5 transition-all group">
      <div className="flex gap-5">
        <div className="w-28 h-28 shrink-0 rounded-[20px] overflow-hidden bg-slate-100 dark:bg-slate-800">
          {place.imageUrls?.[0] ? (
            <img src={place.imageUrls[0]} alt={place.name} className="w-full h-full object-cover transition-transform group-hover:scale-110" />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-slate-300">
              <ImageIcon size={28} />
            </div>
          )}
        </div>

        <div className="flex-1 min-w-0 flex flex-col justify-between py-1">
          <div>
            <div className="flex justify-between items-start">
              <a href={place.placeUrl} target="_blank" rel="noreferrer" className="text-lg font-black truncate group-hover:text-indigo-600 transition-colors flex items-center gap-1.5">
                {place.name} <ExternalLink size={14} className="opacity-0 group-hover:opacity-100 transition-opacity" />
              </a>
            </div>
            <p className="text-xs text-slate-400 font-bold mt-1 flex items-center gap-1">
              <MapPin size={12} className="text-indigo-500" /> {place.address}
            </p>
          </div>

          <div className="flex justify-between items-end mt-4">
            <div className="flex gap-5">
              <div className="flex flex-col">
                <span className="text-[9px] text-slate-400 font-black uppercase tracking-wider">Reviews</span>
                <span className="text-xs font-black">{(place.visitorReviewCount || 0).toLocaleString()}</span>
              </div>
              <div className="flex flex-col">
                <span className="text-[9px] text-slate-400 font-black uppercase tracking-wider">Rating</span>
                <span className="text-xs font-black flex items-center gap-0.5">
                  <Star size={10} className="fill-amber-400 text-amber-400" /> {place.rating?.toFixed(1) || '0.0'}
                </span>
              </div>
            </div>

            <div className="flex flex-col items-end gap-1">
              <span className={`text-[10px] font-black text-indigo-500 transition-opacity ${(hoveredRating || userRating) ? 'opacity-100' : 'opacity-0'}`}>
                {getRatingText(hoveredRating || userRating)}
              </span>
              <div className="flex items-center gap-0.5">
                {[1, 2, 3, 4, 5].map((s) => (
                  <Star 
                    key={s} 
                    size={18} 
                    onMouseEnter={() => setHoveredRating(s)} 
                    onMouseLeave={() => setHoveredRating(0)}
                    onClick={() => setUserRating(s)}
                    className={`cursor-pointer transition-all ${(hoveredRating || userRating || 0) >= s ? 'text-amber-400 fill-amber-400 scale-110' : 'text-slate-200 dark:text-slate-800'}`} 
                  />
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
});

PlaceCard.displayName = 'PlaceCard';