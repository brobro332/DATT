"use client";

import { FormEvent, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";

import { signup } from "@/services/authService";

import { MainLayout } from "@/layouts/MainLayout";
import { Button } from "@/components/common/Button";
import { Card } from "@/components/common/Card";
import { Input } from "@/components/common/Input";

export default function SignupPage() {
  const router = useRouter();

  const [email, setEmail] = useState("");
  const [nickname, setNickname] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");

  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(
    event: FormEvent<HTMLFormElement>,
  ) {
    event.preventDefault();

    if (password !== passwordConfirm) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      setIsLoading(true);

      await signup({
        email,
        nickname,
        password,
      });

      alert("회원가입이 완료되었습니다.");

      router.push("/login");
    } catch (error) {
      console.error(error);

      alert("회원가입에 실패했습니다.");
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
              Join DATT
            </p>

            <h1 className="mt-2 text-3xl font-bold tracking-tight text-gray-950">
              회원가입
            </h1>

            <p className="mt-3 text-sm leading-6 text-gray-600">
              나만의 장소를 저장하고, Anchor를 만들어보세요.
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
              id="nickname"
              label="닉네임"
              type="text"
              placeholder="닉네임을 입력하세요"
              autoComplete="nickname"
              value={nickname}
              onChange={(event) => setNickname(event.target.value)}
            />

            <Input
              id="password"
              label="비밀번호"
              type="password"
              placeholder="비밀번호를 입력하세요"
              autoComplete="new-password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />

            <Input
              id="passwordConfirm"
              label="비밀번호 확인"
              type="password"
              placeholder="비밀번호를 다시 입력하세요"
              autoComplete="new-password"
              value={passwordConfirm}
              onChange={(event) => setPasswordConfirm(event.target.value)}
            />

            <Button
              type="submit"
              className="w-full"
              size="lg"
              disabled={isLoading}
            >
              {isLoading ? "가입 중..." : "회원가입"}
            </Button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-600">
            이미 계정이 있나요?{" "}
            <Link
              href="/login"
              className="font-semibold text-gray-950 underline-offset-4 hover:underline"
            >
              로그인
            </Link>
          </div>
        </Card>
      </div>
    </MainLayout>
  );
}