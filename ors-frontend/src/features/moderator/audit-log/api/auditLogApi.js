import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-50. Audit entry là bất biến, chỉ có list/filter, không có write.
export const auditLogApi = {
  async list(filters = {}) {
    const { data } = await httpClient.get(ENDPOINTS.moderation.auditLogs.list, { params: filters });
    return data;
  },
};
