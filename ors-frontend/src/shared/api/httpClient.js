import axios from 'axios';
import { useAuthStore } from '../../app/store.js';

// <<DataWrapper>> dùng chung: mọi feature phải gọi API qua instance này,
// không import axios trực tiếp trong feature. Đây là nơi duy nhất biết
// base URL, cách đính JWT, và cách xử lý lỗi 401 toàn cục.
export const httpClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '/api',
  timeout: 15000,
});

httpClient.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      useAuthStore.getState().clearSession();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  },
);
