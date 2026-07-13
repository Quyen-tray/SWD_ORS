import { useMutation, useQueryClient } from '@tanstack/react-query';
import { pipelineApi } from '../api/pipelineApi.js';

// <<control>> — UC-05 Schedule Interview, bao gồm Cancel/Reschedule (interviews.status).
export function useInterviewSchedule() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: ['candidates'] });

  const schedule = useMutation({
    mutationFn: pipelineApi.scheduleInterview,
    onSuccess: invalidate,
  });
  const cancel = useMutation({
    mutationFn: pipelineApi.cancelInterview,
    onSuccess: invalidate,
  });
  const reschedule = useMutation({
    mutationFn: ({ interviewId, newTime }) => pipelineApi.rescheduleInterview(interviewId, newTime),
    onSuccess: invalidate,
  });

  return { schedule, cancel, reschedule };
}
