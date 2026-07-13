import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useDebounce } from '../../../../shared/hooks/useDebounce.js';
import { jobSearchApi } from '../api/jobSearchApi.js';

// <<control>> — nghiệp vụ tìm kiếm job (debounce, gọi API, chuẩn hoá state cho UI).
export function useJobSearch() {
  const [keyword, setKeyword] = useState('');
  const debouncedKeyword = useDebounce(keyword);

  const { data, isLoading } = useQuery({
    queryKey: ['jobs', debouncedKeyword],
    queryFn: () => jobSearchApi.search(debouncedKeyword),
  });

  return { jobs: data ?? [], isLoading, keyword, setKeyword };
}
