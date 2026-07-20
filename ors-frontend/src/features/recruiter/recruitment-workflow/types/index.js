/**
 * UC-04 Kanban board: tái dùng đúng CandidateSummaryResponse của UC-01 (xem
 * `candidate-management/types/index.js`) - `pipelineApi.listBoard()` gọi thẳng cùng
 * endpoint GET /recruiter/candidates, không có DTO board riêng ở backend.
 * @typedef {Object} Application
 * @property {number} applicationId
 * @property {string} fullName
 * @property {string} jobTitle
 * @property {number=} rating
 * @property {import('../../../../shared/types/index.js').ApplicationStatus} status
 * @property {string} appliedAt - ISO datetime.
 */

/**
 * @typedef {Object} Interview
 * @property {number} id
 * @property {number} applicationId
 * @property {string} scheduledTime
 * @property {string=} location
 * @property {string=} meetingLink
 * @property {import('../../../../shared/types/index.js').InterviewStatus} status
 */