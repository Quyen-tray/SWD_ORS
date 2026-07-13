import { httpClient } from '../../../../shared/api/httpClient.js';

// <<DataWrapper>> — FE-10 Recruitment Job Management.
export const jobManagementApi = {
  async list() {
    const { data } = await httpClient.get('/recruiter/jobs');
    return data;
  },
};
