import { ReactNode } from "react";
import { GlobalHeader } from "@/components/common/GlobalHeader";

type MainLayoutProps = {
  children: ReactNode;
};

export function MainLayout({ children }: MainLayoutProps) {
  return (
    <div className="min-h-screen bg-white text-gray-900">
      <GlobalHeader />

      <main className="mx-auto w-full max-w-6xl px-4 py-8">
        {children}
      </main>
    </div>
  );
}