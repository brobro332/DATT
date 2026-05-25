"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";

import { login } from "@/services/authService";
import { useAuthStore } from "@/stores/authStore";

import { MainLayout } from "@/layouts/MainLayout";
import { Button } from "@/components/common/Button";
import { Card } from "@/components/common/Card";
import { Input } from "@/components/common/Input";

export default function LoginPage() {
  const router = useRouter();
  const setAuth = useAuthStore((state) => state.setAuth);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(event: { preventDefault: () => void }) {
    event.preventDefault();

    try {
      setIsLoading(true);

      const response = await login({
        email,
        password,
      });

      const member = {
        memberId: response.memberId,
        nickname: response.nickname,
      };

      localStorage.setItem("accessToken", response.accessToken);
      localStorage.setItem("refreshToken", response.refreshToken);
      localStorage.setItem("member", JSON.stringify(member));

      setAuth(response.accessToken, response.refreshToken, member);

      router.push("/");
    } catch (error) {
      console.error(error);
      alert("로그인에 실패했습니다.");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <MainLayout>
      <div className="mx-auto flex min-h-[calc(100vh-160px)] max-w-md items-center">
        <Card className="w-full p-8">
          <div className="mb-8 text-center">
            <p className="text-sm font-semibold text-gray-500">
              Welcome back
            </p>

            <h1 className="mt-2 text-3xl font-bold tracking-tight text-gray-950">
              DATT 로그인
            </h1>

            <p className="mt-3 text-sm leading-6 text-gray-600">
              저장한 장소와 Anchor를 다시 확인해보세요.
            </p>
          </div>

          <form className="space-y-5" onSubmit={handleSubmit}>
            <Input
              id="email"
              label="이메일"
              type="email"
              placeholder="you@example.com"
              autoComplete="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
            />

            <Input
              id="password"
              label="비밀번호"
              type="password"
              placeholder="비밀번호를 입력하세요"
              autoComplete="current-password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />

            <Button
              type="submit"
              className="w-full"
              size="lg"
              disabled={isLoading}
            >
              {isLoading ? "로그인 중..." : "로그인"}
            </Button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-600">
            아직 계정이 없나요?{" "}
            <Link
              href="/signup"
              className="font-semibold text-gray-950 underline-offset-4 hover:underline"
            >
              회원가입
            </Link>
          </div>
        </Card>
      </div>
    </MainLayout>
  );
}