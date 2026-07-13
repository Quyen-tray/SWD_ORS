import { httpClient } from '../../../../shared/api/httpClient.js';
import { ENDPOINTS } from '../../../../shared/api/endpoints.js';

const SUBJECT_PREFIX = '[Subject: ';

// <<DataWrapper>> — UC-08 Send Message, UC-10 Send Email dùng chung 1 endpoint.
export const communicationApi = {
  async list(applicationId) {
    const { data } = await httpClient.get(ENDPOINTS.communications.list(applicationId));
    return data;
  },
  async sendMessage(applicationId, text) {
    const { data } = await httpClient.post(ENDPOINTS.communications.send(applicationId), {
      message: text,
    });
    return data;
  },
  async sendEmail(applicationId, { subject, body }) {
    const { data } = await httpClient.post(ENDPOINTS.communications.send(applicationId), {
      message: `${SUBJECT_PREFIX}${subject}]\n${body}`,
    });
    return data;
  },
  // Parse convention để hiển thị lại type/subject ở UI (UC-11 View Communication History).
  parseMessage(raw) {
    if (raw.startsWith(SUBJECT_PREFIX)) {
      const closeIdx = raw.indexOf(']\n');
      return {
        type: 'EMAIL',
        subject: raw.slice(SUBJECT_PREFIX.length, closeIdx),
        body: raw.slice(closeIdx + 2),
      };
    }
    return { type: 'MESSAGE', subject: null, body: raw };
  },
};
