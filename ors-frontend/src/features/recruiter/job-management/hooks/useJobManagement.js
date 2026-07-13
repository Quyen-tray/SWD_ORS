import { useQuery } from '@tanstack/react-query';
import { jobManagementApi } from '../api/jobManagementApi.js';

export function useJobManagement() {
  const { data, isLoading } = useQuery({ queryKey: ['recruiter-jobs'], queryFn: jobManagementApi.list });
  return { jobs: data ?? [], isLoading };
}
