import { useQuery } from '@tanstack/react-query';
import { JobCategoryManagementApi } from '../api/job_category_managementApi.js';

// <<control>> placeholder cho module job-category-management.
export function useJobCategoryManagement() {
  const { data, isLoading } = useQuery({
    queryKey: ['job-category-management'],
    queryFn: JobCategoryManagementApi.list,
  });
  return { items: data ?? [], isLoading };
}
