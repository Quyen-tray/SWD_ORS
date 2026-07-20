import { useQuery } from '@tanstack/react-query';
import { UserDetailApi } from '../api/user_detailApi.js';

// <<control>> cho module user-detail (UC-62).
export function useUserDetail(id) {
  const { data, isLoading, error } = useQuery({
    queryKey: ['admin', 'users', id],
    queryFn: () => UserDetailApi.detail(id),
    enabled: Boolean(id),
  });

  return {
    user: data,
    history: data?.history ?? [],
    isLoading,
    notFound: error?.response?.status === 404,
  };
}
