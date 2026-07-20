import { useEffect, useState } from 'react';
import { Modal } from '../../../../shared/components/Modal.jsx';
import { Button } from '../../../../shared/components/Button.jsx';
import { useInterviewOutcome } from '../hooks/useInterviewOutcome.js';

const OUTCOME_OPTIONS = [
  { value: 'PASS', label: 'Pass' },
  { value: 'FAIL', label: 'Fail' },
  { value: 'NEED_SECOND_ROUND', label: 'Need Second Round' },
];

function renderStars(rating) {
  const rounded = Math.round(rating * 2) / 2;
  return [1, 2, 3, 4, 5]
    .map((i) => (i <= rounded ? '★' : i - 0.5 === rounded ? '⯨' : '☆'))
    .join('');
}

// <<boundary>> — UC-06 Record Interview Result. Bố cục tham khảo
// frontend_demo/uc06-record-interview-result.html (chip Pass/Fail/Need Second Round + sao
// đánh giá + nhận xét). Thang sao đổi sang <input type="range" step={0.5}> thay vì click-
// theo-nửa-sao của demo tĩnh - vẫn khớp cột interviews.rating decimal(3,1) và hiển thị
// "x.x / 5" như demo, không cần dựng lại UI click nửa-sao bằng tay.
//
// `interview` (prop, do PipelineBoard truyền vào qua interviewsByApplication - xem ghi chú
// GIỚI HẠN ĐÃ BIẾT trong InterviewModal.jsx, cùng hạn chế "mất state sau F5" vẫn còn đó):
// chỉ mở được khi đã có 1 interview (không có nút mở modal này nếu chưa đặt lịch).
// `interview.status` SCHEDULED/RESCHEDULED: chưa có kết quả -> form ghi mới.
// `interview.status` COMPLETED: đã có kết quả -> chỉ xem (backend chặn ghi đè lần 2, xem
// requireOpenInterview trong InterviewService).
// interview.status CANCELLED không có đường vào modal này (PipelineBoard chỉ cho mở khi
// interview đang mở hoặc đã hoàn thành - xem điều kiện hiện nút ở PipelineBoard.jsx).
//
// Submit ở đây KHÔNG đổi job_applications.status - Recruiter tự chuyển giai đoạn pipeline
// riêng qua UC-04/UC-07 sau khi xem kết quả (khớp ghi chú cuối trang trong demo).
export function InterviewResultModal({ open, application, interview, onClose, onChange }) {
  const [outcome, setOutcome] = useState('PASS');
  const [rating, setRating] = useState(0);
  const [comments, setComments] = useState('');
  const { recordOutcome, isSaving, error } = useInterviewOutcome();

  // Nạp lại form trống mỗi khi mở modal cho 1 interview khác.
  useEffect(() => {
    if (!open) return;
    setOutcome('PASS');
    setRating(0);
    setComments('');
  }, [open, interview?.id]);

  if (!open || !interview) return null;

  const isReadOnly = interview.status === 'COMPLETED';

  function handleClose() {
    if (isSaving) return;
    onClose();
  }

  function handleSubmit() {
    recordOutcome(
      { interviewId: interview.id, outcome, rating, comments: comments.trim() || null },
      { onSuccess: (data) => { onChange(data); onClose(); } },
    );
  }

  return (
    <Modal open={open} onClose={handleClose} title={`Kết quả phỏng vấn · Vòng ${interview.round}`}>
      <div style={styles.field}>
        <label style={styles.label}>Ứng viên</label>
        <div>
          <div style={styles.name}>{application?.fullName}</div>
          {application?.jobTitle && <div style={styles.muted}>{application.jobTitle}</div>}
        </div>
      </div>

      {error && <div style={styles.error}>{error}</div>}

      {isReadOnly ? (
        <div style={styles.notice}>
          {OUTCOME_OPTIONS.find((o) => o.value === interview.outcome)?.label ?? interview.outcome}
          {interview.rating != null ? ` · ${renderStars(interview.rating)} ${Number(interview.rating).toFixed(1)} / 5` : ''}
          {interview.comments ? ` — ${interview.comments}` : ''}
        </div>
      ) : (
        <>
          <div style={styles.field}>
            <label style={styles.label}>Kết quả <span style={styles.req}>*</span></label>
            <div style={styles.chipGroup}>
              {OUTCOME_OPTIONS.map((opt) => (
                <button
                  key={opt.value}
                  type="button"
                  style={outcome === opt.value ? { ...styles.chip, ...styles.chipActive } : styles.chip}
                  onClick={() => setOutcome(opt.value)}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>

          <div style={styles.field}>
            <label style={styles.label}>Đánh giá</label>
            <div style={styles.ratingRow}>
              <span style={styles.starsDisplay}>{renderStars(rating)}</span>
              <input
                style={styles.range}
                type="range"
                min={0}
                max={5}
                step={0.5}
                value={rating}
                onChange={(e) => setRating(+e.target.value)}
              />
              <span style={styles.muted}>{rating.toFixed(1)} / 5</span>
            </div>
          </div>

          <div style={styles.field}>
            <label style={styles.label}>Nhận xét</label>
            <textarea
              style={styles.textarea}
              value={comments}
              placeholder="Kỹ năng kỹ thuật tốt, giao tiếp rõ ràng, cần đánh giá thêm về..."
              onChange={(e) => setComments(e.target.value)}
            />
          </div>
        </>
      )}

      <div style={styles.actions}>
        <div style={{ flex: 1 }} />
        <Button variant="ghost" disabled={isSaving} onClick={handleClose}>Đóng</Button>
        {!isReadOnly && (
          <Button variant="primary" disabled={isSaving} onClick={handleSubmit}>
            {isSaving ? 'Đang lưu...' : 'Lưu kết quả'}
          </Button>
        )}
      </div>
    </Modal>
  );
}

const styles = {
  field: { marginBottom: 14 },
  label: { display: 'block', margin: '0 0 6px', fontWeight: 700, fontSize: 13, color: '#111827' },
  req: { color: '#ef4444' },
  name: { fontWeight: 800, fontSize: 14, color: '#0f172a' },
  muted: { color: '#6b7280', fontSize: 12.5 },
  chipGroup: { display: 'flex', gap: 8, flexWrap: 'wrap' },
  chip: {
    border: '1px solid #d1d5db',
    borderRadius: 999,
    padding: '7px 13px',
    fontSize: 12.5,
    fontWeight: 700,
    color: '#475569',
    cursor: 'pointer',
    background: '#fff',
  },
  chipActive: { background: '#00b14f', color: '#fff', borderColor: '#00b14f' },
  ratingRow: { display: 'flex', alignItems: 'center', gap: 10 },
  starsDisplay: { fontSize: 18, color: '#f59e0b', letterSpacing: 2 },
  range: { flex: 1 },
  textarea: {
    width: '100%',
    minHeight: 78,
    padding: '10px 12px',
    borderRadius: 8,
    border: '1px solid #d1d5db',
    resize: 'vertical',
    font: 'inherit',
    boxSizing: 'border-box',
  },
  notice: {
    background: '#fffbeb',
    color: '#92400e',
    padding: '10px 12px',
    borderRadius: 6,
    marginBottom: 14,
    fontSize: 13,
  },
  error: {
    background: '#fee2e2',
    color: '#991b1b',
    padding: '10px 12px',
    borderRadius: 6,
    marginBottom: 14,
    fontSize: 13,
  },
  actions: { display: 'flex', alignItems: 'center', gap: 10, marginTop: 18 },
};