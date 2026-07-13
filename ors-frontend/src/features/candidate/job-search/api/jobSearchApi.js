import { httpClient } from '../../../../shared/api/httpClient.js';

// <<DataWrapper>>
export const jobSearchApi = {
  async search(keyword) {
    const { data } = await httpClient.get('/jobs', { params: { keyword } });
    return data;
  },
};
