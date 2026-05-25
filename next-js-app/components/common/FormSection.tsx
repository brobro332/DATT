import { ReactNode } from "react";

type FormSectionProps = {
  title?: string;
  description?: string;
  children: ReactNode;
};

export function FormSection({
  title,
  description,
  children,
}: FormSectionProps) {
  return (
    <section className="space-y-5 rounded-3xl border border-gray-200 bg-white p-6 shadow-sm">
      {(title || description) && (
        <div>
          {title && (
            <h2 className="text-lg font-bold text-gray-950">
              {title}
            </h2>
          )}

          {description && (
            <p className="mt-2 text-sm leading-6 text-gray-600">
              {description}
            </p>
          )}
        </div>
      )}

      <div className="space-y-4">{children}</div>
    </section>
  );
}