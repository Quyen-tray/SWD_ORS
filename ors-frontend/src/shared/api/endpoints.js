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
  candidates: {
    list: '/recruiter/candidates',
    detail: (id) => `/recruiter/candidates/${id}`,
    downloadCv: (cvId) => `/recruiter/cvs/${cvId}/download`,
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
};
