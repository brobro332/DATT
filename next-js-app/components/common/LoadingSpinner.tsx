type LoadingSpinnerProps = {
  size?: "sm" | "md" | "lg";
};

const sizeClassNames = {
  sm: "h-4 w-4",
  md: "h-6 w-6",
  lg: "h-8 w-8",
};

export function LoadingSpinner({
  size = "md",
}: LoadingSpinnerProps) {
  return (
    <div
      className={[
        "animate-spin rounded-full border-2 border-gray-200 border-t-gray-900",
        sizeClassNames[size],
      ].join(" ")}
      aria-label="loading"
    />
  );
}