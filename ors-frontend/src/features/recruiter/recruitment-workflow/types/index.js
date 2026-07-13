/**
 * @typedef {Object} Application
 * @property {number} id
 * @property {string} candidateName
 * @property {import('../../../../shared/types/index.js').ApplicationStatus} status
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
