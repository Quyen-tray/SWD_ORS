import { useQuery } from '@tanstack/react-query';
import { JobModerationApi } from '../api/job_moderationApi.js';

// <<control>> placeholder cho module job-moderation.
export function useJobModeration() {
  const { data, isLoading } = useQuery({
    queryKey: ['job-moderation'],
    queryFn: JobModerationApi.list,
  });
  return { items: data ?? [], isLoading };
}
