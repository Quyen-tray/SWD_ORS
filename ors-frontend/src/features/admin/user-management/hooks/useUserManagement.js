import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { UserManagementApi } from '../api/user_managementApi.js';
import { useDebounce } from '../../../../shared/hooks/useDebounce.js';

// <<control>> cho module user-management (UC-53..56).
// Giữ tiêu chí tìm kiếm/lọc, gọi API, và làm mới danh sách sau mỗi lần đổi trạng thái.
export function useUserManagement() {
  const queryClient = useQueryClient();

  const [keyword, setKeyword] = useState('');
  const [role, setRole] = useState('');
  const [status, setStatus] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Gõ tới đâu tìm tới đó thì mỗi phím là một request. Chờ người dùng ngừng gõ rồi mới gọi.
  const debouncedKeyword = useDebounce(keyword, 400);

  const filters = { keyword: debouncedKeyword, role, status };

  const { data, isLoading } = useQuery({
    queryKey: ['admin', 'users', filters],
    queryFn: () => UserManagementApi.list(filters),
  });

  // Backend là nơi giữ luật, nên khi nó từ chối thì hiển thị đúng câu nó trả về
  // (vd "Admin không thể tự thao tác lên tài khoản của chính mình") thay vì tự bịa thông báo.
  function handleError(error) {
    setErrorMessage(error?.response?.data?.message ?? 'Có lỗi xảy ra, vui lòng thử lại.');
  }

  function onSuccess() {
    setErrorMessage('');
    queryClient.invalidateQueries({ queryKey: ['admin', 'users'] });
  }

  const activateMutation = useMutation({
    mutationFn: (id) => UserManagementApi.activate(id),
    onSuccess,
    onError: handleError,
  });

  const deactivateMutation = useMutation({
    mutationFn: ({ id, reason }) => UserManagementApi.deactivate(id, reason),
    onSuccess,
    onError: handleError,
  });

  const banMutation = useMutation({
    mutationFn: ({ id, reason }) => UserManagementApi.ban(id, reason),
    onSuccess,
    onError: handleError,
  });

  function resetFilters() {
    setKeyword('');
    setRole('');
    setStatus('');
    setErrorMessage('');
  }

  return {
    users: data ?? [],
    isLoading,
    keyword,
    setKeyword,
    role,
    setRole,
    status,
    setStatus,
    resetFilters,
    errorMessage,
    setErrorMessage,
    activateUser: (id) => activateMutation.mutate(id),
    deactivateUser: (id, reason) => deactivateMutation.mutate({ id, reason }),
    banUser: (id, reason) => banMutation.mutate({ id, reason }),
  };
}
