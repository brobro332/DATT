"use client";

import { ReactNode, useEffect } from "react";
import { useAuthStore } from "@/stores/authStore";

type AuthProviderProps = {
  children: ReactNode;
};

export function AuthProvider({ children }: AuthProviderProps) {
  const restoreAuth = useAuthStore((state) => state.restoreAuth);

  useEffect(() => {
    restoreAuth();
  }, [restoreAuth]);

  return <>{children}</>;
}