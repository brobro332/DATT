import { apiClient } from "@/lib/apiClient";
import type { ApiResponse } from "@/types/api";
import type {
  LoginRequest,
  LoginResponse,
  SignupRequest,
  SignupResponse,
} from "@/types/auth";

export async function signup(
  request: SignupRequest,
): Promise<SignupResponse> {
  const response = await apiClient.post<ApiResponse<SignupResponse>>(
    "/api/auth/signup",
    request,
  );

  return response.data.data;
}

export async function login(
  request: LoginRequest,
): Promise<LoginResponse> {
  const response = await apiClient.post<ApiResponse<LoginResponse>>(
    "/api/auth/login",
    request,
  );

  return response.data.data;
}