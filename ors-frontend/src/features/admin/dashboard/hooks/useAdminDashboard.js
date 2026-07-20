import { useQuery } from '@tanstack/react-query';
import { UserManagementApi } from '../../user-management/api/user_managementApi.js';
import { JobCategoryManagementApi } from '../../job-category-management/api/job_category_managementApi.js';

// <<control>> cho trang Dashboard tổng quan. Không phải UC riêng, chỉ tổng hợp lại
// dữ liệu từ 2 endpoint list đã có (UC-53, UC-58) - không cần endpoint backend mới.
export function useAdminDashboard() {
  const usersQuery = useQuery({ queryKey: ['admin', 'users', {}], queryFn: () => UserManagementApi.list() });
  const categoriesQuery = useQuery({ queryKey: ['admin', 'job-categories'], queryFn: JobCategoryManagementApi.list });

  const users = usersQuery.data ?? [];
  const categories = categoriesQuery.data ?? [];

  const usersByStatus = countBy(users, (u) => u.status);
  const usersByRole = countBy(users, (u) => u.role);

  return {
    isLoading: usersQuery.isLoading || categoriesQuery.isLoading,
    totalUsers: users.length,
    totalCategories: categories.length,
    usersByStatus,
    usersByRole,
  };
}

function countBy(list, keyFn) {
  const counts = {};
  for (const item of list) {
    const key = keyFn(item);
    counts[key] = (counts[key] ?? 0) + 1;
  }
  return counts;
}
