import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> cho module user-detail (UC-62).
export const UserDetailApi = {
  async detail(id) {
    const { data } = await httpClient.get(ENDPOINTS.admin.users.detail(id));
    return data;
  },
};
