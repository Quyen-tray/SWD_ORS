// <<DataAbstraction>> dùng chung. Dự án dùng JS thuần (không TypeScript), nên các
// "type" này được khai báo bằng JSDoc để vẫn có gợi ý kiểu trong IDE mà không
// cần build step riêng. Đây là nơi liệt kê enum khớp đúng với ors_final.sql —
// đổi ở đây khi DB đổi, không hardcode chuỗi trạng thái rải rác trong feature.

/**
 * Khớp job_applications.status trong ors_final.sql (7 giá trị active + WITHDRAWN).
 * @typedef {'SUBMITTED'|'UNDER_REVIEW'|'SHORTLISTED'|'INTERVIEW_SCHEDULED'|
 *   'INTERVIEWED'|'OFFERED'|'HIRED'|'REJECTED'|'WITHDRAWN'} ApplicationStatus
 */

/**
 * Khớp candidate_evaluations.outcome (CHANGE-2 trong ors_final.sql).
 * @typedef {'PASS'|'FAIL'|'SECOND_ROUND'} InterviewOutcome
 */

/**
 * Khớp interviews.status (CHANGE-3 trong ors_final.sql).
 * @typedef {'SCHEDULED'|'RESCHEDULED'|'CANCELLED'|'COMPLETED'} InterviewStatus
 */

/**
 * @typedef {Object} User
 * @property {number} id
 * @property {string} email
 * @property {'CANDIDATE'|'RECRUITER'|'MODERATOR'|'ADMIN'} role
 */

/**
 * @typedef {Object} ApiResponse
 * @property {boolean} success
 * @property {*} data
 * @property {string=} message
 */

export const APPLICATION_STATUS = /** @type {const} */ ({
  SUBMITTED: 'SUBMITTED',
  UNDER_REVIEW: 'UNDER_REVIEW',
  SHORTLISTED: 'SHORTLISTED',
  INTERVIEW_SCHEDULED: 'INTERVIEW_SCHEDULED',
  INTERVIEWED: 'INTERVIEWED',
  OFFERED: 'OFFERED',
  HIRED: 'HIRED',
  REJECTED: 'REJECTED',
  WITHDRAWN: 'WITHDRAWN',
});

export const INTERVIEW_OUTCOME = /** @type {const} */ ({
  PASS: 'PASS',
  FAIL: 'FAIL',
  SECOND_ROUND: 'SECOND_ROUND',
});
