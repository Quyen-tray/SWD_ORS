import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-01 View Candidate List.
// Phase 6: đã xoá getDetail/downloadCv (UC-02/UC-03, ngoài scope đồ án - xem
// 00_KE_HOACH_TONG_QUAN.md) - không có controller nào ở backend khớp 2 path đó, và
// không có nơi nào trong UI gọi tới (đã grep xác nhận trước khi xoá).
export const candidateApi = {
  // UC-01. Chỉ gửi tiêu chí nào thực sự có giá trị, giống UserManagementApi.list,
  // để backend biết khi nào là "không lọc" thay vì lọc theo chuỗi rỗng.
  async list({ keyword, jobPostId, status, page, pageSize } = {}) {
    const params = {};
    if (keyword) params.keyword = keyword;
    if (jobPostId) params.jobPostId = jobPostId;
    if (status) params.status = status;
    params.page = page ?? 1;
    params.pageSize = pageSize ?? 10;
    const { data } = await httpClient.get(ENDPOINTS.candidates.list, { params });
    return data; // { items, total, page, pageSize }
  },
};