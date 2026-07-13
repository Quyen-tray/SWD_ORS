import { httpClient } from '../../../../shared/api/httpClient.js';

// <<DataWrapper>> placeholder cho module user-management.
export const UserManagementApi = {
  async list() {
    const { data } = await httpClient.get('/admin/user-management');
    return data;
  },
};
