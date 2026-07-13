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

  const ROLE_HOME = {
    CANDIDATE: '/candidate/jobs',
    RECRUITER: '/recruiter/dashboard',
    MODERATOR: '/moderator/job-moderation',
    ADMIN: '/admin/users',
  };

  async function login(credentials) {
    setIsLoading(true);
    setError(null);
    try {
      const { user, token } = await authApi.login(credentials);
      setSession(user, token);
      navigate(ROLE_HOME[user.role] ?? '/');
    } catch (err) {
      setError(err.response?.data?.message ?? 'Đăng nhập thất bại.');
    } finally {
      setIsLoading(false);
    }
  }

  return { login, isLoading, error };
}
