import { MainLayout } from "@/layouts/MainLayout";
import { KakaoMap } from "@/components/map/KakaoMap";

export default function MapPage() {
  return (
    <MainLayout>
      <section>
        <h1 className="text-2xl font-bold">지도 탐색</h1>
        <p className="mt-2 text-sm text-gray-600">
          기준 위치를 중심으로 주변 장소와 Anchor를 탐색합니다.
        </p>

        <div className="mt-6">
          <KakaoMap />
        </div>
      </section>
    </MainLayout>
  );
}