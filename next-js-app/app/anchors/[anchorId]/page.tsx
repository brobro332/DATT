import { MainLayout } from "@/layouts/MainLayout";

type AnchorDetailPageProps = {
  params: {
    anchorId: string;
  };
};

export default function AnchorDetailPage({
  params,
}: AnchorDetailPageProps) {
  return (
    <MainLayout>
      <h1 className="text-2xl font-bold">Anchor 상세</h1>
      <p className="mt-2 text-gray-600">anchorId: {params.anchorId}</p>
    </MainLayout>
  );
}