import { httpClient } from '../../../../shared/api/httpClient.js';

// <<DataWrapper>> placeholder cho module company-verification.
export const CompanyVerificationApi = {
  async list() {
    const { data } = await httpClient.get('/moderator/company-verification');
    return data;
  },
};
