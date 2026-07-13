import { useQuery } from '@tanstack/react-query';
import { communicationApi } from '../api/communicationApi.js';

// <<control>> — UC-11 View Communication History.
export function useCommunicationHistory(applicationId) {
  const { data, isLoading } = useQuery({
    queryKey: ['communications', applicationId],
    queryFn: () => communicationApi.list(applicationId),
    enabled: Boolean(applicationId),
  });

  const parsed = (data ?? []).map((item) => ({
    ...item,
    ...communicationApi.parseMessage(item.message),
  }));

  return { items: parsed, isLoading };
}
