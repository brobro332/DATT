type FormErrorMessageProps = {
  message?: string;
};

export function FormErrorMessage({
  message,
}: FormErrorMessageProps) {
  if (!message) {
    return null;
  }

  return (
    <p className="rounded-2xl bg-red-50 px-4 py-3 text-sm font-medium text-red-600">
      {message}
    </p>
  );
}