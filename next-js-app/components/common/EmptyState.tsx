import { ReactNode } from "react";

type EmptyStateProps = {
  title?: string;
  description?: string;
  action?: ReactNode;
};

export function EmptyState({
  title = "데이터가 없습니다.",
  description = "조건을 변경하거나 다시 시도해보세요.",
  action,
}: EmptyStateProps) {
  return (
    <div className="rounded-3xl border border-gray-200 bg-white p-8 text-center">
      <p className="text-lg font-bold text-gray-950">
        {title}
      </p>

      <p className="mt-2 text-sm leading-6 text-gray-500">
        {description}
      </p>

      {action && <div className="mt-5">{action}</div>}
    </div>
  );
}