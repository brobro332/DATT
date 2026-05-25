type ErrorStateProps = {
  title?: string;
  message?: string;
  onRetry?: () => void;
};

export function ErrorState({
  title = "문제가 발생했습니다.",
  message = "잠시 후 다시 시도해주세요.",
  onRetry,
}: ErrorStateProps) {
  return (
    <div className="rounded-3xl border border-red-100 bg-red-50 p-8 text-center">
      <p className="text-lg font-bold text-red-700">
        {title}
      </p>

      <p className="mt-2 text-sm leading-6 text-red-600">
        {message}
      </p>

      {onRetry && (
        <button
          type="button"
          onClick={onRetry}
          className="mt-5 rounded-full bg-red-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-red-700"
        >
          다시 시도
        </button>
      )}
    </div>
  );
}