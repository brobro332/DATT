export type ApiResponse<T> = {
  success: boolean;
  data: T;
  message?: string;
};

export type PageResponse<T> = {
  content: T[];
  pageable: unknown;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
};