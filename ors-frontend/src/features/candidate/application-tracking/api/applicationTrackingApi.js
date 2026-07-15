import httpClient from '../../../../shared/api/httpClient';
import { ENDPOINTS } from '../../../../shared/api/endpoints';

export const applicationTrackingApi = {
    // UC-71: Lịch sử ứng tuyển
    getApplications: async (candidateId) => {
        const response = await httpClient.get(ENDPOINTS.candidate.applications(candidateId));
        return response.data;
    },

    // UC-71: Rút đơn
    withdrawApplication: async (appId) => {
        const response = await httpClient.patch(ENDPOINTS.candidate.withdraw(appId));
        return response.data;
    },

    // UC-73: Dashboard stats
    getDashboardStats: async (candidateId) => {
        const response = await httpClient.get(ENDPOINTS.candidate.dashboard(candidateId));
        return response.data;
    },

    // UC-70: Nộp đơn
    applyForJob: async (candidateId, jobPostId, cvId) => {
        const response = await httpClient.post(`${ENDPOINTS.candidate.apply(candidateId)}?jobPostId=${jobPostId}&cvId=${cvId}`);
        return response.data;
    },
    
    // UC-72: Notifications Settings
    updateNotificationSettings: async (candidateId, settings) => {
        const response = await httpClient.put(ENDPOINTS.candidate.notifications(candidateId), settings);
        return response.data;
    }
};
