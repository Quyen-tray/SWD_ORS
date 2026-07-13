import { useState } from 'react';
import { Modal } from '../../../../shared/components/Modal.jsx';
import { useInterviewOutcome } from '../hooks/useInterviewOutcome.js';

// <<boundary>> — UC-06 Record Interview Result.
// Lưu ý: submit ở đây KHÔNG đổi pipeline status của candidate (xem hook).
export function InterviewResultModal({ open, onClose, interviewId }) {
  const [outcome, setOutcome] = useState('PASS');
  const [rating, setRating] = useState(3);
  const [comments, setComments] = useState('');
  const { recordOutcome, isSaving } = useInterviewOutcome();

  function handleSubmit() {
    recordOutcome({ interviewId, outcome, rating, comments });
    onClose();
  }

  return (
    <Modal open={open} onClose={onClose} title="Ghi nhận kết quả phỏng vấn">
      <select value={outcome} onChange={(e) => setOutcome(e.target.value)}>
        <option value="PASS">Pass</option>
        <option value="FAIL">Fail</option>
        <option value="SECOND_ROUND">Need Second Round</option>
      </select>
      <input type="number" min={1} max={5} value={rating} onChange={(e) => setRating(+e.target.value)} />
      <textarea value={comments} onChange={(e) => setComments(e.target.value)} placeholder="Nhận xét" />
      <button onClick={handleSubmit} disabled={isSaving}>Lưu kết quả</button>
    </Modal>
  );
}
