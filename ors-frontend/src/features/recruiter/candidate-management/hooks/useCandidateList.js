import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { candidateApi } from '../api/candidateApi.js';
import { useDebounce } from '../../../../shared/hooks/useDebounce.js';

const PAGE_SIZE = 10;

// <<control>> — UC-01 View Candidate List: tìm theo từ khoá (tên/email), lọc theo
// trạng thái, phân trang. jobPostId chưa có UI lọc riêng vì frontend chưa có API nào
// liệt kê job post của recruiter đang đăng nhập (module job_management ngoài scope
// đồ án của UC-01 - xem 00_KE_HOACH_TONG_QUAN.md mục 1b); giữ tham số này trong hook để
// dùng lại ngay khi module đó có endpoint, không phải sửa lại hook.
export function useCandidateList() {
  const [keyword, setKeyword] = useState('');
  const [status, setStatus] = useState('');
  const [page, setPage] = useState(1);
  // Chưa có UI lọc theo job post (xem ghi chú ở đầu file) nên luôn gửi null; giữ field
  // này trong filters/queryKey để candidateApi.list và query cache không phải sửa lại
  // khi module job_management có endpoint liệt kê job post.
  const jobPostId = null;

  // Gõ tới đâu tìm tới đó thì mỗi phím là một request. Chờ người dùng ngừng gõ rồi mới gọi,
  // giống hệt useUserManagement bên admin.
  const debouncedKeyword = useDebounce(keyword, 400);

  const filters = { keyword: debouncedKeyword, status, jobPostId, page, pageSize: PAGE_SIZE };

  const { data, isLoading, error } = useQuery({
    queryKey: ['recruiter', 'candidates', filters],
    queryFn: () => candidateApi.list(filters),
  });

  // Đổi tiêu chí lọc thì phải quay về trang 1, không thì có thể đứng ở trang không còn
  // dữ liệu (vd đang ở trang 3, lọc còn 1 trang).
  function updateKeyword(value) {
    setKeyword(value);
    setPage(1);
  }
  function updateStatus(value) {
    setStatus(value);
    setPage(1);
  }
  function resetFilters() {
    setKeyword('');
    setStatus('');
    setPage(1);
  }

  const total = data?.total ?? 0;
  const totalPages = Math.max(1, Math.ceil(total / PAGE_SIZE));

  return {
    candidates: data?.items ?? [],
    total,
    totalPages,
    isLoading,
    error: error?.response?.data?.message,
    keyword,
    setKeyword: updateKeyword,
    status,
    setStatus: updateStatus,
    resetFilters,
    page,
    setPage,
    pageSize: PAGE_SIZE,
  };
}