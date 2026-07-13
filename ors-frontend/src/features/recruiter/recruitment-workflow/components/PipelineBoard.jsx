import { usePipelineStatus } from '../hooks/usePipelineStatus.js';

// <<boundary>> — Kanban hiển thị theo 7 trạng thái active của job_applications.status.
// Có thể gom nhóm cột để gọn UI (vd 1 cột "Pending" gồm SUBMITTED + UNDER_REVIEW)
// nhưng khi kéo-thả phải set đúng 1 giá trị cụ thể, không set giá trị gộp.
const COLUMNS = [
  { status: 'SUBMITTED', label: 'Submitted' },
  { status: 'UNDER_REVIEW', label: 'Under Review' },
  { status: 'SHORTLISTED', label: 'Shortlisted' },
  { status: 'INTERVIEW_SCHEDULED', label: 'Interview Scheduled' },
  { status: 'INTERVIEWED', label: 'Interviewed' },
  { status: 'OFFERED', label: 'Offered' },
  { status: 'HIRED', label: 'Hired' },
];

export function PipelineBoard({ applications }) {
  const { updateStatus, error } = usePipelineStatus();

  function handleReject(applicationId) {
    const reason = window.prompt('Nhập lý do từ chối (bắt buộc):');
    if (!reason) return; // usePipelineStatus sẽ tự validate lại lần nữa
    updateStatus({ applicationId, status: 'REJECTED', reason });
  }

  return (
    <div>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <div style={{ display: 'flex', gap: 12, overflowX: 'auto' }}>
        {COLUMNS.map((col) => (
          <div key={col.status} style={{ minWidth: 220 }}>
            <h4>{col.label}</h4>
            {applications
              .filter((a) => a.status === col.status)
              .map((a) => (
                <div key={a.id} style={{ border: '1px solid #e5e7eb', padding: 8, marginBottom: 8 }}>
                  {a.candidateName}
                  <button onClick={() => handleReject(a.id)}>Reject</button>
                </div>
              ))}
          </div>
        ))}
      </div>
    </div>
  );
}
