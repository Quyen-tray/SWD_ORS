/**
 * Khớp CandidateSummaryResponse bên backend (candidate_management, UC-01).
 * @typedef {Object} CandidateListItem
 * @property {number} applicationId
 * @property {string} fullName
 * @property {string} jobTitle
 * @property {number=} cvId
 * @property {string=} cvName
 * @property {number=} rating - 0..5, có thể null nếu recruiter chưa đánh giá.
 * @property {import('../../../../shared/types/index.js').ApplicationStatus} status
 * @property {string} appliedAt - ISO datetime.
 */

/**
 * Khớp CandidateListResponse bên backend: { items, total, page, pageSize }.
 * @typedef {Object} CandidateListResult
 * @property {CandidateListItem[]} items
 * @property {number} total
 * @property {number} page
 * @property {number} pageSize
 */