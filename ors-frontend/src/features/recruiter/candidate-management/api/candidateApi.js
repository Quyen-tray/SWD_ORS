import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-01, UC-02, UC-03 (Export Candidate List, sau khi đổi số theo bản UC mới).
export const candidateApi = {
  async list(params) {
    const { data } = await httpClient.get(ENDPOINTS.candidates.list, { params });
    return data; // { items, total, page }
  },
  async getDetail(id) {
    const { data } = await httpClient.get(ENDPOINTS.candidates.detail(id));
    return data;
  },
  async downloadCv(cvId) {
    const { data } = await httpClient.get(ENDPOINTS.candidates.downloadCv(cvId), {
      responseType: 'blob',
    });
    return data;
  },
};
