import { useQuery } from '@tanstack/react-query';
import { pipelineApi } from '../api/pipelineApi.js';

// <<control>> — UC-04: nạp dữ liệu cho Kanban board (tách khỏi usePipelineStatus vì đó
// là hook cho hành động đổi trạng thái, còn đây là hook đọc dữ liệu - cùng khuôn với
// useCandidateList/candidateApi bên UC-01). queryKey bắt đầu bằng 'recruiter' giống
// useCandidateList để dễ đối chiếu, nhưng tách nhánh 'pipeline' vì đây là truy vấn
// "lấy hết" (không phân trang/lọc) chứ không phải cùng 1 query với danh sách có phân
// trang của UC-01 - 2 query khác nhau nên phải khác key, dù cùng gọi 1 endpoint backend.
export function usePipelineBoard() {
  const { data, isLoading, error, refetch } = useQuery({
    queryKey: ['recruiter', 'pipeline', 'board'],
    queryFn: () => pipelineApi.listBoard(),
  });

  return {
    applications: data ?? [],
    isLoading,
    error: error?.response?.data?.message,
    refetch,
  };
}