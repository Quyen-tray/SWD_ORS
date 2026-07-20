import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> cho module audit-log (UC-61).
export const AuditLogApi = {
  async list() {
    const { data } = await httpClient.get(ENDPOINTS.admin.auditLogs.list);
    return data;
  },
};
