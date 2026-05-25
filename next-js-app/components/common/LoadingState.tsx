import { LoadingSpinner } from "@/components/common/LoadingSpinner";

type LoadingStateProps = {
  message?: string;
};

export function LoadingState({
  message = "불러오는 중입니다...",
}: LoadingStateProps) {
  return (
    <div className="flex min-h-40 flex-col items-center justify-center rounded-3xl border border-gray-200 bg-white p-8 text-center">
      <LoadingSpinner />

      <p className="mt-4 text-sm font-medium text-gray-500">
        {message}
      </p>
    </div>
  );
}