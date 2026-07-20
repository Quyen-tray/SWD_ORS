import { useMutation, useQueryClient } from '@tanstack/react-query';
import { pipelineApi } from '../api/pipelineApi.js';

// <<control>> — UC-06 Record Interview Result. Ghi outcome độc lập với pipeline
// status: đúng thực tế "phỏng vấn hết ứng viên rồi mới chọn" (đã chốt trong UC spec).
// Không có side-effect nào tự đổi status của job_applications ở đây (xem
// InterviewService.recordOutcome, Phase 4a - chỉ tự đổi interviews.status sang COMPLETED).
// Bug đã sửa (Phase 4b, cùng loại bug đã sửa ở usePipelineStatus.js/Phase 2b và
// useInterviewSchedule.js/Phase 3b): bản cũ invalidate ['candidates'] - không khớp query
// key thật nào (['recruiter','candidates',...] của useCandidateList,
// ['recruiter','pipeline','board'] của usePipelineBoard) nên sau khi ghi kết quả, cả bảng
// UC-01 lẫn Kanban UC-04 đều không tự refetch (dù thật ra không có field nào trên 2 màn đó
// đổi vì kết quả không đụng status - vẫn sửa cho nhất quán quy ước với 2 hook kia).
export function useInterviewOutcome() {
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: ({ interviewId, outcome, rating, comments }) =>
      pipelineApi.recordOutcome(interviewId, { outcome, rating, comments }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['recruiter', 'candidates'] });
      queryClient.invalidateQueries({ queryKey: ['recruiter', 'pipeline'] });
    },
  });

  return {
    recordOutcome: mutation.mutate,
    isSaving: mutation.isPending,
    error: mutation.error?.response?.data?.message ?? mutation.error?.message,
  };
}