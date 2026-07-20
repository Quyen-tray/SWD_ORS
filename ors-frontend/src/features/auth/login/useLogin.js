import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../authApi.js';
import { useAuthStore } from '../../../app/store.js';

// <<control>> — nghiệp vụ đăng nhập: gọi API, lưu session, điều hướng theo role.
export function useLogin() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const setSession = useAuthStore((s) => s.setSession);
  const navigate = useNavigate();

  // RECRUITER: trỏ vào /recruiter/candidates (UC-01), không phải /recruiter/dashboard -
  // route đó không tồn tại trong recruiterRoutes.jsx (chỉ có `index` khớp đúng
  // "/recruiter", không có path "dashboard") và backend RecruiterDashboardController
  // (UC-34) chỉ là scaffold rỗng, ngoài phạm vi 5 UC đã hoàn thiện. candidates là bước
  // đầu tiên thật sự dùng được trong luồng UC-01 → UC-04 → UC-05 → UC-06 → UC-07.
  const ROLE_HOME = {
    CANDIDATE: '/candidate/jobs',
    RECRUITER: '/recruiter/candidates',
    MODERATOR: '/moderator/job-moderation',
    ADMIN: '/admin/users',
  };

  async function login(credentials) {
    setIsLoading(true);
    setError(null);
    try {
      const tokenResponse = await authApi.login(credentials);
      const user = {
        id: tokenResponse.userId ?? 0,
        email: credentials.email,
        role: tokenResponse.role ?? 'RECRUITER',
      };
      setSession(user, tokenResponse.accessToken);
      navigate(ROLE_HOME[user.role] ?? '/');
    } catch (err) {
      setError(err.response?.data?.message ?? 'Đăng nhập thất bại.');
    } finally {
      setIsLoading(false);
    }
  }

  return { login, isLoading, error };
}
