import { useMutation, useQueryClient } from '@tanstack/react-query';
import { pipelineApi } from '../api/pipelineApi.js';

// <<control>> — UC-05 Schedule Interview, bao gồm Cancel/Reschedule (interviews.status).
// Bug đã sửa (Phase 3b, cùng loại bug đã sửa ở usePipelineStatus.js/Phase 2b): bản cũ
// invalidate ['candidates'] - không khớp query key thật nào (['recruiter','candidates',...]
// của useCandidateList, ['recruiter','pipeline','board'] của usePipelineBoard) nên sau khi
// đặt/đổi/hủy lịch, cả bảng UC-01 lẫn Kanban UC-04 đều không tự refetch.
// 3 mutation tách riêng (thay vì gộp 1 mutation nhận thêm "action") để InterviewModal có
// isPending/error riêng cho từng nút bấm (Hủy lịch / Lưu thay đổi / Gửi lời mời) - tránh
// bấm 1 nút mà cả 3 nút cùng disable hoặc hiện lỗi sai chỗ.
export function useInterviewSchedule() {
  const queryClient = useQueryClient();
  const invalidate = () => {
    queryClient.invalidateQueries({ queryKey: ['recruiter', 'candidates'] });
    queryClient.invalidateQueries({ queryKey: ['recruiter', 'pipeline'] });
  };

  const scheduleMutation = useMutation({
    mutationFn: pipelineApi.scheduleInterview,
    onSuccess: invalidate,
  });
  const cancelMutation = useMutation({
    mutationFn: pipelineApi.cancelInterview,
    onSuccess: invalidate,
  });
  const rescheduleMutation = useMutation({
    mutationFn: ({ interviewId, newTime }) => pipelineApi.rescheduleInterview(interviewId, newTime),
    onSuccess: invalidate,
  });

  return {
    scheduleInterview: scheduleMutation.mutate,
    isScheduling: scheduleMutation.isPending,
    scheduleError: scheduleMutation.error?.response?.data?.message ?? scheduleMutation.error?.message,

    cancelInterview: cancelMutation.mutate,
    isCancelling: cancelMutation.isPending,
    cancelError: cancelMutation.error?.response?.data?.message ?? cancelMutation.error?.message,

    rescheduleInterview: rescheduleMutation.mutate,
    isRescheduling: rescheduleMutation.isPending,
    rescheduleError: rescheduleMutation.error?.response?.data?.message ?? rescheduleMutation.error?.message,
  };
}