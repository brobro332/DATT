export type SignupRequest = {
  email: string;
  password: string;
  nickname: string;
};

export type LoginRequest = {
  email: string;
  password: string;
};

export type LoginResponse = {
  accessToken: string;
  refreshToken: string;
  memberId: number;
  nickname: string;
};

export type SignupResponse = {
  memberId: number;
  email: string;
  nickname: string;
};

export type TokenReissueResponse = {
  accessToken: string;
};