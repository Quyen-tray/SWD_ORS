import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { auditLogApi } from '../api/auditLogApi.js';

// <<control>> — UC-50: lọc theo entityType/entityId/actionType + "chỉ hành động của tôi".
export function useAuditLog() {
  const [entityType, setEntityType] = useState('');
  const [entityId, setEntityId] = useState('');
  const [actionType, setActionType] = useState('');
  const [mineOnly, setMineOnly] = useState(false);

  const filters = {
    entityType: entityType || undefined,
    entityId: entityId || undefined,
    actionType: actionType || undefined,
    mineOnly,
  };

  const { data, isLoading, error } = useQuery({
    queryKey: ['moderation', 'auditLogs', filters],
    queryFn: () => auditLogApi.list(filters),
  });

  return {
    entries: data ?? [],
    isLoading,
    error: error?.response?.data?.message,
    entityType,
    setEntityType,
    entityId,
    setEntityId,
    actionType,
    setActionType,
    mineOnly,
    setMineOnly,
  };
}
