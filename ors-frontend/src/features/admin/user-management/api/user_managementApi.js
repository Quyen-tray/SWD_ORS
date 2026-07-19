import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> cho module user-management (UC-53..56).
// Chỉ biết cách gọi API, không chứa business rule: mọi luật của BR-13 (không tự thao tác
// lên chính mình, bắt buộc lý do, chỉ chuyển trạng thái hợp lệ) đều do backend quyết định.
export const UserManagementApi = {
  // UC-53. Chỉ gửi tiêu chí nào thực sự có giá trị, để backend biết khi nào là "không lọc".
  async list({ keyword, role, status } = {}) {
    const params = {};
    if (keyword) params.keyword = keyword;
    if (role) params.role = role;
    if (status) params.status = status;
    const { data } = await httpClient.get(ENDPOINTS.admin.users.list, { params });
    return data;
  },

  // UC-54
  async activate(id) {
    await httpClient.put(ENDPOINTS.admin.users.activate(id));
  },

  // UC-55
  async deactivate(id, reason) {
    await httpClient.put(ENDPOINTS.admin.users.deactivate(id), { reason });
  },

  // UC-56
  async ban(id, reason) {
    await httpClient.put(ENDPOINTS.admin.users.ban(id), { reason });
  },
};
