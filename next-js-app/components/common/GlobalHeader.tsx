"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";

import { useAuthStore } from "@/stores/authStore";
import { logout } from "@/utils/auth";

const navigationItems = [
  {
    href: "/places",
    label: "장소",
  },
  {
    href: "/anchors",
    label: "Anchor",
  },
  {
    href: "/map",
    label: "지도",
  },
  {
    href: "/my/bookmarks",
    label: "내 저장",
  },
];

export function GlobalHeader() {
  const router = useRouter();

  const isLoggedIn = useAuthStore((state) => state.isLoggedIn);
  const member = useAuthStore((state) => state.member);

  function handleLogout() {
    logout();
    router.push("/login");
  }

  return (
    <header className="sticky top-0 z-50 border-b border-gray-200 bg-white/90 backdrop-blur-md">
      <div className="mx-auto flex h-16 w-full max-w-6xl items-center justify-between px-4">
        <Link href="/" className="flex items-center gap-2">
          <div className="flex h-9 w-9 items-center justify-center rounded-2xl bg-gray-900 text-lg font-bold text-white">
            ⚓
          </div>

          <div className="leading-tight">
            <p className="text-lg font-bold tracking-tight text-gray-900">
              DATT
            </p>
            <p className="text-xs text-gray-500">
              Discover All The Town
            </p>
          </div>
        </Link>

        <nav className="hidden items-center gap-6 text-sm font-medium text-gray-600 md:flex">
          {navigationItems.map((item) => (
            <Link
              key={item.href}
              href={item.href}
              className="transition hover:text-gray-950"
            >
              {item.label}
            </Link>
          ))}
        </nav>

        <div className="flex items-center gap-2">
          {isLoggedIn ? (
            <>
              <Link
                href="/my/profile"
                className="hidden rounded-full px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-100 sm:inline-flex"
              >
                {member?.nickname ?? "내 프로필"}
              </Link>

              <button
                type="button"
                onClick={handleLogout}
                className="rounded-full bg-gray-900 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-gray-700"
              >
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link
                href="/login"
                className="hidden rounded-full px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-100 sm:inline-flex"
              >
                로그인
              </Link>

              <Link
                href="/signup"
                className="rounded-full bg-gray-900 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-gray-700"
              >
                회원가입
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}