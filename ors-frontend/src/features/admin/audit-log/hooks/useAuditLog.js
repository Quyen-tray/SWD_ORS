import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { AuditLogApi } from '../api/audit_logApi.js';

// <<control>> cho module audit-log (UC-61).
export function useAuditLog() {
  const [keyword, setKeyword] = useState('');

  const { data, isLoading } = useQuery({ queryKey: ['admin', 'audit-logs'], queryFn: AuditLogApi.list });
  const logs = data ?? [];

  const q = keyword.trim().toLowerCase();
  const filtered = q
    ? logs.filter((l) => l.userEmail?.toLowerCase().includes(q) || l.actionType?.toLowerCase().includes(q))
    : logs;

  return { logs: filtered, isLoading, keyword, setKeyword };
}
