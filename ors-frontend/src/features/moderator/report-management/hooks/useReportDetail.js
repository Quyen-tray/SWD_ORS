import { useQuery } from '@tanstack/react-query';
import { reportManagementApi } from '../api/reportManagementApi.js';

// <<control>> — UC-46: chi tiết 1 report + lịch sử kiểm duyệt.
export function useReportDetail(reportId) {
  const { data, isLoading, error, refetch } = useQuery({
    queryKey: ['moderation', 'reports', reportId],
    queryFn: () => reportManagementApi.getDetail(reportId),
    enabled: Boolean(reportId),
  });

  return { report: data, isLoading, error: error?.response?.data?.message, refetch };
}
