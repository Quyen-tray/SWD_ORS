/**
 * @typedef {Object} ReportSummary
 * @property {number} id
 * @property {number} jobPostId
 * @property {string} jobPostTitle
 * @property {string} reporterEmail
 * @property {string} createdAt
 * @property {import('../../../../shared/types/index.js').ReportStatus} status
 * @property {boolean} slaOverdue
 */

/**
 * @typedef {Object} ModerationHistoryEntry
 * @property {number} id
 * @property {string} createdAt
 * @property {number} moderatorId
 * @property {string} actionType
 * @property {string=} entityType
 * @property {number=} entityId
 * @property {string} description
 */

/**
 * @typedef {Object} ReportDetail
 * @property {number} id
 * @property {string} reason
 * @property {import('../../../../shared/types/index.js').ReportStatus} status
 * @property {string} createdAt
 * @property {boolean} slaOverdue
 * @property {{id: number, title: string, description: string}|null} reportedEntity
 * @property {boolean} entityMissing
 * @property {ModerationHistoryEntry[]} moderationHistory
 */
