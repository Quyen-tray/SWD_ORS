// Nhãn hiển thị cho actionType của audit_logs. Phải đồng bộ với các actionType mà
// AuditLogService.record() ghi ở backend (UserService, JobCategoryService).
// Dùng chung ở AuditLogPage (UC-61) và UserDetailPage (UC-62).
export const AUDIT_ACTION_LABEL = {
  ACTIVATE_USER: 'Kích hoạt người dùng',
  DEACTIVATE_USER: 'Tạm ngưng người dùng',
  BAN_USER: 'Cấm người dùng',
  CREATE_CATEGORY: 'Tạo danh mục',
  UPDATE_CATEGORY: 'Sửa danh mục',
  DELETE_CATEGORY: 'Xoá danh mục',
};
