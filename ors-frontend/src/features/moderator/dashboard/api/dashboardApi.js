import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-49.
export const moderationDashboardApi = {
  async getSummary() {
    const { data } = await httpClient.get(ENDPOINTS.moderation.dashboard.summary);
    return data;
  },
};
