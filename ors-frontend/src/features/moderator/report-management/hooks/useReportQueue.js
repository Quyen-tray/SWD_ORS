import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { reportManagementApi } from '../api/reportManagementApi.js';

// <<control>> — UC-45: lọc theo status + sắp xếp theo ngày tạo.
export function useReportQueue() {
  const [status, setStatus] = useState('');
  const [sort, setSort] = useState('date_desc');

  const { data, isLoading, error } = useQuery({
    queryKey: ['moderation', 'reports', { status, sort }],
    queryFn: () => reportManagementApi.getQueue({ status, sort }),
  });

  return {
    reports: data ?? [],
    isLoading,
    error: error?.response?.data?.message,
    status,
    setStatus,
    sort,
    setSort,
  };
}
