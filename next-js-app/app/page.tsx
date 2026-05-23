import { MainLayout } from "@/layouts/MainLayout";
import { Button } from "@/components/common/Button";
import { Card } from "@/components/common/Card";
import { SectionTitle } from "@/components/common/SectionTitle";

export default function HomePage() {
  return (
    <MainLayout>
      <section className="rounded-3xl bg-gray-50 p-10">
        <SectionTitle
          eyebrow="Discover All The Town"
          title="어디에 닻을 내릴까요?"
          description="DATT는 특정 지역을 기준으로 맛집, 카페, 술집, 숙소, 놀거리를 한 번에 탐색할 수 있는 경험 큐레이션 서비스입니다."
        />

        <div className="flex gap-3">
          <Button>Anchor 만들기</Button>
          <Button variant="secondary">장소 둘러보기</Button>
        </div>
      </section>

      <section className="mt-8 grid gap-4 md:grid-cols-3">
        <Card>
          <p className="text-sm font-semibold text-gray-500">Search</p>
          <h3 className="mt-2 text-lg font-bold">장소 검색</h3>
          <p className="mt-2 text-sm text-gray-600">
            키워드와 지역을 기준으로 장소를 탐색합니다.
          </p>
        </Card>

        <Card>
          <p className="text-sm font-semibold text-gray-500">Anchor</p>
          <h3 className="mt-2 text-lg font-bold">경험 큐레이션</h3>
          <p className="mt-2 text-sm text-gray-600">
            기준 지역 주변의 장소를 카테고리별로 추천합니다.
          </p>
        </Card>

        <Card>
          <p className="text-sm font-semibold text-gray-500">Growth</p>
          <h3 className="mt-2 text-lg font-bold">사용자 성장</h3>
          <p className="mt-2 text-sm text-gray-600">
            리뷰, 저장, Anchor 생성 활동을 성장 데이터로 기록합니다.
          </p>
        </Card>
      </section>
    </MainLayout>
  );
}