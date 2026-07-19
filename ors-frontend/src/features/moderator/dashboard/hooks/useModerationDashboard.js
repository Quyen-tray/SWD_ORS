import { useQuery } from '@tanstack/react-query';
import { moderationDashboardApi } from '../api/dashboardApi.js';

// <<control>> — UC-49. refetchInterval ngắn vì số report mở/SLA% đổi liên tục.
export function useModerationDashboard() {
  const { data, isLoading, error } = useQuery({
    queryKey: ['moderation', 'dashboard'],
    queryFn: moderationDashboardApi.getSummary,
    refetchInterval: 30_000,
  });

  return { summary: data, isLoading, error: error?.response?.data?.message };
}
