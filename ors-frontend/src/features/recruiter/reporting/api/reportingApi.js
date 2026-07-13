import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-13 View Recruitment Statistics, UC-14 Export Statistics Report.
export const reportingApi = {
  async getStats(params) {
    const { data } = await httpClient.get(ENDPOINTS.reporting.stats, { params });
    return data;
  },
  async exportReport(params) {
    const { data } = await httpClient.get(ENDPOINTS.reporting.export, {
      params,
      responseType: 'blob',
    });
    return data;
  },
};
