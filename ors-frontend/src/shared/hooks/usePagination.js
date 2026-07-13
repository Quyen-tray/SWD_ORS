import { useState } from 'react';

export function usePagination(initialPage = 1) {
  const [page, setPage] = useState(initialPage);
  const [pageSize] = useState(10);
  return { page, pageSize, setPage };
}
