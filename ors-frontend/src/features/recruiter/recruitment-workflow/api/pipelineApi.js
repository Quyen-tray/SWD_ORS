import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-04 (Update Pipeline Status), UC-05 (Schedule Interview),
// UC-06 (Record Interview Result), UC-07 (Hire Candidate) theo số UC đã đánh lại.
export const pipelineApi = {
  // UC-04: nạp toàn bộ ứng viên cho Kanban board. Chưa có endpoint riêng "liệt kê tất cả
  // application của recruiter" nên tái dùng thẳng GET /recruiter/candidates (UC-01) với
  // pageSize đủ lớn để lấy hết trong 1 lần thay vì phân trang (board hiển thị theo cột
  // trạng thái, không theo trang) - RecruiterCandidateService không giới hạn pageSize
  // (xem service, chỉ fallback về 10 khi pageSize <= 0). Không lọc status ở query để lấy
  // đủ cả REJECTED cho cột "Rejected"; WITHDRAWN vẫn trả về nhưng board tự ẩn đi (không
  // thuộc 8 cột trong frontend_demo/uc04-pipeline-status.html).
  async listBoard() {
    const { data } = await httpClient.get(ENDPOINTS.candidates.list, {
      params: { page: 1, pageSize: 500 },
    });
    return data.items ?? [];
  },

  // UC-04 / UC-07: status phải là 1 trong 7 giá trị active của job_applications.status.
  // reason bắt buộc khi status = 'REJECTED' (application_status_histories.reason).
  async updateStatus(applicationId, { status, reason }) {
    const { data } = await httpClient.patch(ENDPOINTS.applications.updateStatus(applicationId), {
      status,
      reason,
    });
    return data;
  },

  // UC-05
  async scheduleInterview(payload) {
    const { data } = await httpClient.post(ENDPOINTS.interviews.schedule, payload);
    return data;
  },
  async cancelInterview(interviewId) {
    // Phase 3b: đổi từ discard response sang trả về data - InterviewController.cancelInterview
    // trả InterviewResponse (status=CANCELLED), UI cần nó để cập nhật state tại chỗ mà không
    // phải gọi lại GET /interviews/{id} riêng.
    const { data } = await httpClient.post(ENDPOINTS.interviews.cancel(interviewId));
    return data;
  },
  async rescheduleInterview(interviewId, newTime) {
    const { data } = await httpClient.post(ENDPOINTS.interviews.reschedule(interviewId), {
      scheduledTime: newTime,
    });
    return data;
  },

  // UC-06: outcome KHÔNG tự động đổi job_applications.status (xem UC spec đã cập nhật).
  async recordOutcome(interviewId, { outcome, rating, comments }) {
    const { data } = await httpClient.post(ENDPOINTS.interviews.recordOutcome(interviewId), {
      outcome,
      rating,
      comments,
    });
    return data;
  },
};