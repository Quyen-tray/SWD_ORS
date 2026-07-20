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
 * Khớp interviews.outcome / CK_interviews_outcome trong db.sql (patch Phase 0). Sửa ở
 * Phase 4b: bản cũ ghi 'SECOND_ROUND' - sai với CHECK constraint thật (backend luôn 400).
 * @typedef {'PASS'|'FAIL'|'NEED_SECOND_ROUND'} InterviewOutcome
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

/**
 * Khớp job_reports.status ở backend (Moderation, UC-45..48). Cột DB là NVARCHAR tự do,
 * không CHECK - enum này chỉ để tránh hardcode chuỗi rải rác ở feature moderator.
 * @typedef {'PENDING'|'UNDER_INVESTIGATION'|'RESOLVED'|'CLOSED'} ReportStatus
 */

/**
 * UC-47 - một report có thể chọn nhiều hành động xử lý cùng lúc (AF-01).
 * @typedef {'REMOVE_POSTING'|'SUSPEND_COMPANY'|'ISSUE_WARNING'} EnforcementType
 */

/**
 * UC-48 - lý do đóng report, bắt buộc chọn.
 * @typedef {'NO_VIOLATION_FOUND'|'DUPLICATE_REPORT'|'REPORTER_ERROR'|
 *   'ENTITY_ALREADY_REMOVED'|'OTHER'} ClosureReason
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

export const REPORT_STATUS = /** @type {const} */ ({
  PENDING: 'PENDING',
  UNDER_INVESTIGATION: 'UNDER_INVESTIGATION',
  RESOLVED: 'RESOLVED',
  CLOSED: 'CLOSED',
});

export const ENFORCEMENT_TYPE = /** @type {const} */ ({
  REMOVE_POSTING: 'REMOVE_POSTING',
  SUSPEND_COMPANY: 'SUSPEND_COMPANY',
  ISSUE_WARNING: 'ISSUE_WARNING',
});

export const CLOSURE_REASON = /** @type {const} */ ({
  NO_VIOLATION_FOUND: 'NO_VIOLATION_FOUND',
  DUPLICATE_REPORT: 'DUPLICATE_REPORT',
  REPORTER_ERROR: 'REPORTER_ERROR',
  ENTITY_ALREADY_REMOVED: 'ENTITY_ALREADY_REMOVED',
  OTHER: 'OTHER',
});