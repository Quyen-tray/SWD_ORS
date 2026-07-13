import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-12 View Recruitment Dashboard.
export const dashboardApi = {
  async getSummary() {
    const { data } = await httpClient.get(ENDPOINTS.dashboard.summary);
    return data;
  },
};
