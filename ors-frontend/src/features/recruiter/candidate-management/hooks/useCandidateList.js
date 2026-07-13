import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { candidateApi } from '../api/candidateApi.js';

// <<control>> — UC-01 View Candidate List: filter theo job/status + phân trang.
export function useCandidateList() {
  const [filters, setFilters] = useState({ jobPostId: null, status: null, page: 1 });

  const { data, isLoading } = useQuery({
    queryKey: ['candidates', filters],
    queryFn: () => candidateApi.list(filters),
  });

  return {
    candidates: data?.items ?? [],
    total: data?.total ?? 0,
    isLoading,
    filters,
    setFilters,
  };
}
