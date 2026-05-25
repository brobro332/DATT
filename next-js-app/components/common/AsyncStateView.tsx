import { ReactNode } from "react";
import { LoadingState } from "@/components/common/LoadingState";
import { ErrorState } from "@/components/common/ErrorState";
import { EmptyState } from "@/components/common/EmptyState";

type AsyncStateViewProps = {
  isLoading: boolean;
  isError: boolean;
  isEmpty?: boolean;

  loadingMessage?: string;
  errorTitle?: string;
  errorMessage?: string;
  emptyTitle?: string;
  emptyDescription?: string;

  children: ReactNode;
};

export function AsyncStateView({
  isLoading,
  isError,
  isEmpty = false,

  loadingMessage,
  errorTitle,
  errorMessage,
  emptyTitle,
  emptyDescription,

  children,
}: AsyncStateViewProps) {
  if (isLoading) {
    return <LoadingState message={loadingMessage} />;
  }

  if (isError) {
    return (
      <ErrorState
        title={errorTitle}
        message={errorMessage}
      />
    );
  }

  if (isEmpty) {
    return (
      <EmptyState
        title={emptyTitle}
        description={emptyDescription}
      />
    );
  }

  return <>{children}</>;
}