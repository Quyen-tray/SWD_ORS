import { httpClient } from '../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../shared/api/endpoints.js';

// <<DataWrapper>> cho toàn bộ nhóm auth (FE-09: Logout, Forgot/Reset/Change Password).
export const authApi = {
  async login(credentials) {
    const { data } = await httpClient.post(ENDPOINTS.auth.login, credentials);
    return data; // { user, token }
  },
  async logout() {
    await httpClient.post(ENDPOINTS.auth.logout);
  },
  async forgotPassword(email) {
    await httpClient.post(ENDPOINTS.auth.forgotPassword, { email });
  },
  async resetPassword(payload) {
    await httpClient.post(ENDPOINTS.auth.resetPassword, payload);
  },
  async changePassword(payload) {
    await httpClient.post(ENDPOINTS.auth.changePassword, payload);
  },
};
