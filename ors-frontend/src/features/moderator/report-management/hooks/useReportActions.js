import { useMutation, useQueryClient } from '@tanstack/react-query';
import { reportManagementApi } from '../api/reportManagementApi.js';

// <<control>> — UC-46/47/48: investigate/resolve/close. Validate tối thiểu ở đây trước khi
// gọi API (khớp thông điệp validate ở ReportService phía backend) để phản hồi nhanh hơn -
// backend vẫn là nơi chặn thật.
export function useReportActions(reportId) {
  const queryClient = useQueryClient();

  function invalidateAll() {
    queryClient.invalidateQueries({ queryKey: ['moderation', 'reports'] });
    queryClient.invalidateQueries({ queryKey: ['moderation', 'dashboard'] });
    queryClient.invalidateQueries({ queryKey: ['moderation', 'auditLogs'] });
  }

  const investigateMutation = useMutation({
    mutationFn: () => reportManagementApi.investigate(reportId),
    onSuccess: invalidateAll,
  });

  const resolveMutation = useMutation({
    mutationFn: ({ enforcementActions, resolutionSummary }) => {
      if (!enforcementActions?.length) {
        throw new Error('Phải chọn ít nhất một hành động xử lý.');
      }
      if (!resolutionSummary?.trim()) {
        throw new Error('Phải nhập tóm tắt xử lý.');
      }
      return reportManagementApi.resolve(reportId, { enforcementActions, resolutionSummary });
    },
    onSuccess: invalidateAll,
  });

  const closeMutation = useMutation({
    mutationFn: ({ closureReason, note }) => {
      if (!closureReason) {
        throw new Error('Phải chọn lý do đóng report.');
      }
      return reportManagementApi.close(reportId, { closureReason, note });
    },
    onSuccess: invalidateAll,
  });

  return {
    investigate: investigateMutation.mutate,
    isInvestigating: investigateMutation.isPending,
    investigateError: investigateMutation.error?.response?.data?.message ?? investigateMutation.error?.message,

    resolve: resolveMutation.mutate,
    isResolving: resolveMutation.isPending,
    resolveError: resolveMutation.error?.response?.data?.message ?? resolveMutation.error?.message,

    close: closeMutation.mutate,
    isClosing: closeMutation.isPending,
    closeError: closeMutation.error?.response?.data?.message ?? closeMutation.error?.message,
  };
}
