// Tập trung tất cả URL backend ở 1 chỗ — khi backend đổi path, chỉ sửa ở đây.
export const ENDPOINTS = {
  auth: {
    login: '/auth/login',
    logout: '/auth/logout',
    forgotPassword: '/auth/forgot-password',
    resetPassword: '/auth/reset-password',
    changePassword: '/auth/change-password',
    registerRecruiter: '/auth/register/recruiter',
  },
  // Admin (FE-07) — UC-53..60.
  admin: {
    users: {
      list: '/admin/user-management',
      activate: (id) => `/admin/user-management/${id}/activate`,
      deactivate: (id) => `/admin/user-management/${id}/deactivate`, // body: { reason }
      ban: (id) => `/admin/user-management/${id}/ban`, // body: { reason }
    },
    jobCategories: {
      list: '/admin/job-category-management',
      create: '/admin/job-category-management', // body: { categoryName }
      update: (id) => `/admin/job-category-management/${id}`, // body: { categoryName }
      remove: (id) => `/admin/job-category-management/${id}`,
    },
  },
  candidates: {
    list: '/recruiter/candidates',
  },
  applications: {
    updateStatus: (id) => `/applications/${id}/status`, // body: { status, reason? }
    history: (id) => `/applications/${id}/status-history`,
  },
  interviews: {
    schedule: '/interviews',
    detail: (id) => `/interviews/${id}`,
    cancel: (id) => `/interviews/${id}/cancel`,
    reschedule: (id) => `/interviews/${id}/reschedule`,
    recordOutcome: (id) => `/interviews/${id}/evaluation`, // { outcome, rating, comments }
  },
  communications: {
    list: (applicationId) => `/applications/${applicationId}/communications`,
    send: (applicationId) => `/applications/${applicationId}/communications`,
  },
  dashboard: {
    summary: '/recruiter/dashboard/summary',
  },
  reporting: {
    stats: '/recruiter/reports/stats',
    export: '/recruiter/reports/export',
  },
  candidate: {
    profile: (userId) => `/candidate/cvs/profile/${userId}`,
    applications: (candidateId) => `/candidate/applications/${candidateId}`,
    apply: (candidateId) => `/candidate/applications/${candidateId}/apply`,
    withdraw: (appId) => `/candidate/applications/${appId}/withdraw`,
    dashboard: (candidateId) => `/candidate/applications/${candidateId}/dashboard`,
    notifications: (candidateId) => `/candidate/applications/${candidateId}/notifications`,
    savedJobs: (candidateId) => `/candidate/jobs/saved/${candidateId}`,
    toggleSavedJob: (candidateId) => `/candidate/jobs/saved/${candidateId}/toggle`,
  },
  // Moderation (2.3) — UC-45..50, Report Moderation.
  moderation: {
    dashboard: {
      summary: '/moderation/dashboard',
    },
    reports: {
      queue: '/moderation/reports', // query: ?status=&sort=
      detail: (id) => `/moderation/reports/${id}`,
      investigate: (id) => `/moderation/reports/${id}/investigate`,
      resolve: (id) => `/moderation/reports/${id}/resolve`, // body: { enforcementActions, resolutionSummary }
      close: (id) => `/moderation/reports/${id}/close`, // body: { closureReason, note }
    },
    auditLogs: {
      list: '/moderation/audit-logs', // query: ?moderatorId=&entityType=&entityId=&actionType=&from=&to=&mineOnly=
    },
  },
};