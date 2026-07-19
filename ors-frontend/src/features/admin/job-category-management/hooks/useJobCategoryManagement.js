import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { JobCategoryManagementApi } from '../api/job_category_managementApi.js';

// <<control>> cho module job-category-management (UC-57..60).
export function useJobCategoryManagement() {
  const queryClient = useQueryClient();
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const { data, isLoading } = useQuery({
    queryKey: ['admin', 'job-categories'],
    queryFn: JobCategoryManagementApi.list,
  });

  // Backend giữ luật BR-14, nên khi nó từ chối thì hiện đúng câu nó trả về
  // (vd 'Không thể xoá: danh mục "..." đang được tin tuyển dụng sử dụng').
  function handleError(error) {
    setSuccessMessage('');
    setErrorMessage(error?.response?.data?.message ?? 'Có lỗi xảy ra, vui lòng thử lại.');
  }

  function onSuccess(message) {
    return () => {
      setErrorMessage('');
      setSuccessMessage(message);
      queryClient.invalidateQueries({ queryKey: ['admin', 'job-categories'] });
    };
  }

  const createMutation = useMutation({
    mutationFn: (categoryName) => JobCategoryManagementApi.create(categoryName),
    onSuccess: onSuccess('Đã tạo danh mục mới.'),
    onError: handleError,
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, categoryName }) => JobCategoryManagementApi.update(id, categoryName),
    onSuccess: onSuccess('Đã cập nhật danh mục.'),
    onError: handleError,
  });

  const removeMutation = useMutation({
    mutationFn: (id) => JobCategoryManagementApi.remove(id),
    onSuccess: onSuccess('Đã xoá danh mục.'),
    onError: handleError,
  });

  function clearMessages() {
    setErrorMessage('');
    setSuccessMessage('');
  }

  return {
    categories: data ?? [],
    isLoading,
    errorMessage,
    successMessage,
    clearMessages,
    createCategory: (name) => createMutation.mutate(name),
    updateCategory: (id, name) => updateMutation.mutate({ id, categoryName: name }),
    removeCategory: (id) => removeMutation.mutate(id),
  };
}
