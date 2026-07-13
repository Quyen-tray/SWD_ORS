import { useQuery } from '@tanstack/react-query';
import { reportingApi } from '../api/reportingApi.js';

// <<control>> — UC-13. avgTimeToHire tính từ application_status_histories.changed_at
// (applied_at -> lúc status = HIRED), backend trả sẵn số liệu đã tổng hợp.
export function useReportingStats(filters) {
  const { data, isLoading } = useQuery({
    queryKey: ['reporting-stats', filters],
    queryFn: () => reportingApi.getStats(filters),
  });
  return { stats: data, isLoading };
}
