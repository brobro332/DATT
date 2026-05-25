import { InputHTMLAttributes } from "react";
import { Label } from "@/components/common/Label";

type InputProps = InputHTMLAttributes<HTMLInputElement> & {
  label?: string;
  errorMessage?: string;
};

export function Input({
  label,
  errorMessage,
  className = "",
  id,
  ...props
}: InputProps) {
  return (
    <div className="space-y-2">
      {label && <Label htmlFor={id}>{label}</Label>}

      <input
        id={id}
        className={[
          "h-11 w-full rounded-2xl border border-gray-300 bg-white px-4 text-sm outline-none transition",
          "placeholder:text-gray-400",
          "focus:border-gray-900 focus:ring-2 focus:ring-gray-900/10",
          errorMessage
            ? "border-red-400 focus:border-red-500 focus:ring-red-500/10"
            : "",
          className,
        ].join(" ")}
        {...props}
      />

      {errorMessage && (
        <p className="text-sm text-red-500">
          {errorMessage}
        </p>
      )}
    </div>
  );
}