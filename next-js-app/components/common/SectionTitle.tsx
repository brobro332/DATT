type SectionTitleProps = {
  eyebrow?: string;
  title: string;
  description?: string;
};

export function SectionTitle({
  eyebrow,
  title,
  description,
}: SectionTitleProps) {
  return (
    <div className="mb-6">
      {eyebrow && (
        <p className="mb-2 text-sm font-semibold text-gray-500">
          {eyebrow}
        </p>
      )}

      <h2 className="text-2xl font-bold tracking-tight text-gray-950">
        {title}
      </h2>

      {description && (
        <p className="mt-2 max-w-2xl text-sm leading-6 text-gray-600">
          {description}
        </p>
      )}
    </div>
  );
}