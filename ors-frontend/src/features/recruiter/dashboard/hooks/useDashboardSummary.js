import { useQuery } from '@tanstack/react-query';
import { dashboardApi } from '../api/dashboardApi.js';

// <<control>> — gộp số liệu để hiển thị dashboard.
export function useDashboardSummary() {
  const { data, isLoading } = useQuery({
    queryKey: ['dashboard-summary'],
    queryFn: dashboardApi.getSummary,
  });
  return { summary: data, isLoading };
}
