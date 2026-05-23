import { MainLayout } from "@/layouts/MainLayout";

type PlaceDetailPageProps = {
  params: {
    placeId: string;
  };
};

export default function PlaceDetailPage({
  params,
}: PlaceDetailPageProps) {
  return (
    <MainLayout>
      <h1 className="text-2xl font-bold">장소 상세</h1>
      <p className="mt-2 text-gray-600">placeId: {params.placeId}</p>
    </MainLayout>
  );
}