import { useState } from 'react';
import { Modal } from '../../../../shared/components/Modal.jsx';
import { useInterviewSchedule } from '../hooks/useInterviewSchedule.js';

// <<boundary>> — UC-05 Schedule Interview (+ Cancel/Reschedule).
export function InterviewModal({ open, onClose, applicationId }) {
  const [scheduledTime, setScheduledTime] = useState('');
  const { schedule } = useInterviewSchedule();

  function handleSubmit() {
    schedule.mutate({ applicationId, scheduledTime });
    onClose();
  }

  return (
    <Modal open={open} onClose={onClose} title="Đặt lịch phỏng vấn">
      <input
        type="datetime-local"
        value={scheduledTime}
        onChange={(e) => setScheduledTime(e.target.value)}
      />
      <button onClick={handleSubmit} disabled={schedule.isPending}>Lưu lịch</button>
    </Modal>
  );
}
