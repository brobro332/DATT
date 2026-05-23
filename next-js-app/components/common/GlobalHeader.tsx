import Link from "next/link";

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
            시작하기
          </Link>
        </div>
      </div>
    </header>
  );
}