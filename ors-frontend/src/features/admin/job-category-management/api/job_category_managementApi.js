import { httpClient } from '../../../../shared/api/httpClient.js';

// <<DataWrapper>> placeholder cho module job-category-management.
export const JobCategoryManagementApi = {
  async list() {
    const { data } = await httpClient.get('/admin/job-category-management');
    return data;
  },
};
