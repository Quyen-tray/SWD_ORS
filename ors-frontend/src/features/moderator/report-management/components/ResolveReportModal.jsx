import { useState } from 'react';
import { Modal } from '../../../../shared/components/Modal.jsx';
import { ENFORCEMENT_TYPE } from '../../../../shared/types/index.js';
import { ErrorBanner, AccentButton, BRAND } from '../../shared/ui.jsx';

const ACTION_LABELS = {
  REMOVE_POSTING: { icon: '🗑️', label: 'Remove Posting', hint: 'Ẩn tin tuyển dụng khỏi hệ thống' },
  SUSPEND_COMPANY: { icon: '⛔', label: 'Suspend Company', hint: 'Tạm ngưng xác minh công ty' },
  ISSUE_WARNING: { icon: '⚠️', label: 'Issue Warning', hint: 'Ghi nhận cảnh cáo, không đổi trạng thái entity' },
};

// <<boundary>> — UC-47 (AF-01): cho phép chọn nhiều hành động xử lý cùng lúc.
export function ResolveReportModal({ open, onClose, onSubmit, isSubmitting, error }) {
  const [actions, setActions] = useState([]);
  const [summary, setSummary] = useState('');

  function toggle(type) {
    setActions((prev) => (prev.includes(type) ? prev.filter((a) => a !== type) : [...prev, type]));
  }

  function handleSubmit() {
    onSubmit({ enforcementActions: actions, resolutionSummary: summary });
  }

  return (
    <Modal open={open} onClose={onClose} title="✅ Resolve Report">
      <div style={{ display: 'flex', flexDirection: 'column', gap: 10, width: 360 }}>
        <label style={{ fontSize: 12, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>
          Hành động xử lý (chọn ít nhất 1)
        </label>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {Object.values(ENFORCEMENT_TYPE).map((type) => {
            const meta = ACTION_LABELS[type];
            const checked = actions.includes(type);
            return (
              <label
                key={type}
                style={{
                  display: 'flex', alignItems: 'center', gap: 10, padding: '10px 12px',
                  border: `1px solid ${checked ? BRAND.accent : '#e2e8f0'}`,
                  background: checked ? BRAND.accentSoft : '#fff',
                  borderRadius: 8, cursor: 'pointer', transition: 'all 0.15s',
                }}
              >
                <input type="checkbox" checked={checked} onChange={() => toggle(type)} />
                <span>{meta.icon}</span>
                <div>
                  <div style={{ fontWeight: 600, fontSize: 14, color: '#0f172a' }}>{meta.label}</div>
                  <div style={{ fontSize: 12, color: '#94a3b8' }}>{meta.hint}</div>
                </div>
              </label>
            );
          })}
        </div>

        <label style={{ fontSize: 12, fontWeight: 700, color: '#64748b', textTransform: 'uppercase', marginTop: 4 }}>
          Tóm tắt xử lý (bắt buộc)
        </label>
        <textarea
          placeholder="Mô tả ngắn gọn kết luận và hành động đã thực hiện..."
          value={summary}
          onChange={(e) => setSummary(e.target.value)}
          rows={4}
          style={{ padding: 10, borderRadius: 8, border: '1px solid #e2e8f0', fontFamily: 'inherit', fontSize: 14 }}
        />
        <ErrorBanner message={error} />
        <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end', marginTop: 4 }}>
          <AccentButton variant="plain" onClick={onClose}>Huỷ</AccentButton>
          <AccentButton onClick={handleSubmit} disabled={isSubmitting}>
            {isSubmitting ? 'Đang xử lý...' : 'Xác nhận Resolve'}
          </AccentButton>
        </div>
      </div>
    </Modal>
  );
}
