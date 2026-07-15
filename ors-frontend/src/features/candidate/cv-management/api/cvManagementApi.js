import httpClient from '../../../../shared/api/httpClient';
import { ENDPOINTS } from '../../../../shared/api/endpoints';

export const cvManagementApi = {
    // UC-66: Xem hồ sơ
    getProfile: async (userId) => {
        const response = await httpClient.get(ENDPOINTS.candidate.profile(userId));
        return response.data;
    },

    // UC-66: Cập nhật hồ sơ
    updateProfile: async (userId, data) => {
        const response = await httpClient.put(ENDPOINTS.candidate.profile(userId), data);
        return response.data;
    },

    // TODO: UC-67 - uploadCv, createCv, updateCv, deleteCv, duplicateCv, toggleVisibility
};
