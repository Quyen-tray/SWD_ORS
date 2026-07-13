import { useQuery } from '@tanstack/react-query';
import { UserManagementApi } from '../api/user_managementApi.js';

// <<control>> placeholder cho module user-management.
export function useUserManagement() {
  const { data, isLoading } = useQuery({
    queryKey: ['user-management'],
    queryFn: UserManagementApi.list,
  });
  return { items: data ?? [], isLoading };
}
