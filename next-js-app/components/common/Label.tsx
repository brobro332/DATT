import { LabelHTMLAttributes, ReactNode } from "react";

type LabelProps = LabelHTMLAttributes<HTMLLabelElement> & {
  children: ReactNode;
};

export function Label({
  children,
  className = "",
  ...props
}: LabelProps) {
  return (
    <label
      className={[
        "text-sm font-medium text-gray-700",
        className,
      ].join(" ")}
      {...props}
    >
      {children}
    </label>
  );
}