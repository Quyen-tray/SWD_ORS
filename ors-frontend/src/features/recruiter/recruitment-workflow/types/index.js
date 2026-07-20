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
 * Khớp InterviewResponse bên backend (UC-05, xem InterviewResponse.java) - dùng chung cho
 * cả 4 endpoint schedule/detail/cancel/reschedule. outcome/rating/comments luôn null cho
 * tới khi UC-06 (Phase 4a) ghi kết quả, nhưng vẫn khai báo đủ ở đây để InterviewModal
 * (Phase 3b) và màn UC-06 (Phase 4b) dùng chung 1 typedef, không phải khai lại.
 * @typedef {Object} Interview
 * @property {number} id
 * @property {number} applicationId
 * @property {string} scheduledTime - ISO-8601 có offset/"Z".
 * @property {string=} location
 * @property {string=} meetingLink
 * @property {import('../../../../shared/types/index.js').InterviewStatus} status
 * @property {number} round
 * @property {import('../../../../shared/types/index.js').InterviewOutcome=} outcome
 * @property {number=} rating
 * @property {string=} comments
 */