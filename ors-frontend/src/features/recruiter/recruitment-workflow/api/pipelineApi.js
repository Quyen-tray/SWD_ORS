import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

// <<DataWrapper>> — UC-04 (Update Pipeline Status), UC-05 (Schedule Interview),
// UC-06 (Record Interview Result), UC-07 (Hire Candidate) theo số UC đã đánh lại.
export const pipelineApi = {
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
    await httpClient.post(ENDPOINTS.interviews.cancel(interviewId));
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
