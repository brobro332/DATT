import { TextareaHTMLAttributes } from "react";
import { Label } from "@/components/common/Label";

type TextareaProps = TextareaHTMLAttributes<HTMLTextAreaElement> & {
  label?: string;
  errorMessage?: string;
};

export function Textarea({
  label,
  errorMessage,
  className = "",
  id,
  ...props
}: TextareaProps) {
  return (
    <div className="space-y-2">
      {label && <Label htmlFor={id}>{label}</Label>}

      <textarea
        id={id}
        className={[
          "min-h-32 w-full resize-none rounded-2xl border border-gray-300 bg-white px-4 py-3 text-sm outline-none transition",
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