import { useAuthStore } from "@/stores/authStore";

export function logout() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  localStorage.removeItem("member");

  useAuthStore.getState().clearAuth();
}