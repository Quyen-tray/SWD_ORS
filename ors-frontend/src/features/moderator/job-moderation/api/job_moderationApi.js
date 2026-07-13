import { httpClient } from '../../../../shared/api/httpClient.js';

// <<DataWrapper>> placeholder cho module job-moderation.
export const JobModerationApi = {
  async list() {
    const { data } = await httpClient.get('/moderator/job-moderation');
    return data;
  },
};
