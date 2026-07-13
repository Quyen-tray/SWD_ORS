import { useMutation, useQueryClient } from '@tanstack/react-query';
import { pipelineApi } from '../api/pipelineApi.js';

// <<control>> — UC-06 Record Interview Result. Ghi outcome độc lập với pipeline
// status: đúng thực tế "phỏng vấn hết ứng viên rồi mới chọn" (đã chốt trong UC spec).
// Không có side-effect nào tự đổi status của job_applications ở đây.
export function useInterviewOutcome() {
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: ({ interviewId, outcome, rating, comments }) =>
      pipelineApi.recordOutcome(interviewId, { outcome, rating, comments }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['candidates'] });
    },
  });

  return {
    recordOutcome: mutation.mutate,
    isSaving: mutation.isPending,
  };
}
