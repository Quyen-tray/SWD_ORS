import { useMutation, useQueryClient } from '@tanstack/react-query';
import { pipelineApi } from '../api/pipelineApi.js';

// <<control>> — UC-04/UC-07: đổi trạng thái pipeline. Validate "reason bắt buộc
// khi Reject" nằm ở ĐÂY (business logic), không nằm trong component UI.
export function usePipelineStatus() {
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: ({ applicationId, status, reason }) => {
      if (status === 'REJECTED' && !reason?.trim()) {
        throw new Error('Cần nhập lý do khi từ chối ứng viên.');
      }
      return pipelineApi.updateStatus(applicationId, { status, reason });
    },
    onSuccess: () => {
      // Khớp đúng queryKey thật đang dùng (useCandidateList: ['recruiter','candidates',...],
      // usePipelineBoard: ['recruiter','pipeline','board']) - bản cũ invalidate ['candidates']
      // không khớp key nào nên UC-01 lẫn Kanban đều không tự refetch sau khi đổi trạng thái.
      queryClient.invalidateQueries({ queryKey: ['recruiter', 'candidates'] });
      queryClient.invalidateQueries({ queryKey: ['recruiter', 'pipeline'] });
    },
  });

  return {
    updateStatus: mutation.mutate,
    isUpdating: mutation.isPending,
    error: mutation.error?.message,
  };
}