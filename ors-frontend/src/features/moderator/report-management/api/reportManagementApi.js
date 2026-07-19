import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-45..48.
export const reportManagementApi = {
  async getQueue({ status, sort } = {}) {
    const { data } = await httpClient.get(ENDPOINTS.moderation.reports.queue, {
      params: { status: status || undefined, sort: sort || undefined },
    });
    return data;
  },

  async getDetail(id) {
    const { data } = await httpClient.get(ENDPOINTS.moderation.reports.detail(id));
    return data;
  },

  async investigate(id) {
    await httpClient.put(ENDPOINTS.moderation.reports.investigate(id));
  },

  async resolve(id, { enforcementActions, resolutionSummary }) {
    await httpClient.put(ENDPOINTS.moderation.reports.resolve(id), { enforcementActions, resolutionSummary });
  },

  async close(id, { closureReason, note }) {
    await httpClient.put(ENDPOINTS.moderation.reports.close(id), { closureReason, note });
  },
};
