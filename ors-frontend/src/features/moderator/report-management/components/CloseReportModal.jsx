import { useState } from 'react';
import { Modal } from '../../../../shared/components/Modal.jsx';
import { CLOSURE_REASON } from '../../../../shared/types/index.js';
import { ErrorBanner, AccentButton } from '../../shared/ui.jsx';

const inputStyle = { padding: 10, borderRadius: 8, border: '1px solid #e2e8f0', fontFamily: 'inherit', fontSize: 14 };

// <<boundary>> — UC-48.
export function CloseReportModal({ open, onClose, onSubmit, isSubmitting, error }) {
  const [reason, setReason] = useState('');
  const [note, setNote] = useState('');

  function handleSubmit() {
    onSubmit({ closureReason: reason || null, note });
  }

  return (
    <Modal open={open} onClose={onClose} title="✖ Close Report">
      <div style={{ display: 'flex', flexDirection: 'column', gap: 10, width: 360 }}>
        <label style={{ fontSize: 12, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>
          Lý do đóng report (bắt buộc)
        </label>
        <select style={inputStyle} value={reason} onChange={(e) => setReason(e.target.value)}>
          <option value="">-- Chọn lý do --</option>
          {Object.values(CLOSURE_REASON).map((r) => (
            <option key={r} value={r}>{r}</option>
          ))}
        </select>

        <label style={{ fontSize: 12, fontWeight: 700, color: '#64748b', textTransform: 'uppercase', marginTop: 4 }}>
          Ghi chú (không bắt buộc)
        </label>
        <textarea
          placeholder="Chi tiết thêm nếu cần..."
          value={note}
          onChange={(e) => setNote(e.target.value)}
          rows={3}
          style={inputStyle}
        />
        <ErrorBanner message={error} />
        <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end', marginTop: 4 }}>
          <AccentButton variant="plain" onClick={onClose}>Huỷ</AccentButton>
          <AccentButton variant="danger" onClick={handleSubmit} disabled={isSubmitting}>
            {isSubmitting ? 'Đang xử lý...' : 'Xác nhận Close'}
          </AccentButton>
        </div>
      </div>
    </Modal>
  );
}
