import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> cho module job-category-management (UC-57..60).
// Hai luật của BR-14 (tên phải duy nhất, không xoá danh mục còn tin tuyển dụng dùng)
// do backend giữ, tầng này chỉ chuyển tiếp lời từ chối lên trên.
export const JobCategoryManagementApi = {
  // UC-58
  async list() {
    const { data } = await httpClient.get(ENDPOINTS.admin.jobCategories.list);
    return data;
  },

  // UC-57
  async create(categoryName) {
    const { data } = await httpClient.post(ENDPOINTS.admin.jobCategories.create, { categoryName });
    return data;
  },

  // UC-59
  async update(id, categoryName) {
    const { data } = await httpClient.put(ENDPOINTS.admin.jobCategories.update(id), { categoryName });
    return data;
  },

  // UC-60
  async remove(id) {
    await httpClient.delete(ENDPOINTS.admin.jobCategories.remove(id));
  },
};
