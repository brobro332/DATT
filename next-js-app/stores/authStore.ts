import { create } from "zustand";

type AuthMember = {
  memberId: number;
  email: string;
  nickname: string;
};

type AuthState = {
  accessToken: string | null;
  member: AuthMember | null;

  isLoggedIn: boolean;

  setAuth: (accessToken: string, member?: AuthMember | null) => void;
  setMember: (member: AuthMember | null) => void;
  clearAuth: () => void;
};

export const useAuthStore = create<AuthState>((set) => ({
  accessToken: null,
  member: null,

  isLoggedIn: false,

  setAuth: (accessToken, member = null) =>
    set({
      accessToken,
      member,
      isLoggedIn: true,
    }),

  setMember: (member) =>
    set({
      member,
    }),

  clearAuth: () =>
    set({
      accessToken: null,
      member: null,
      isLoggedIn: false,
    }),
}));