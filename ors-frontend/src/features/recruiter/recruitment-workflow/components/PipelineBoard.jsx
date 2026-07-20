import { useEffect, useState } from 'react';
import { usePipelineBoard } from '../hooks/usePipelineBoard.js';
import { usePipelineStatus } from '../hooks/usePipelineStatus.js';
import { Modal } from '../../../../shared/components/Modal.jsx';
import { Button } from '../../../../shared/components/Button.jsx';
import { APPLICATION_STATUS } from '../../../../shared/types/index.js';
import { InterviewModal } from './InterviewModal.jsx';
import { InterviewResultModal } from './InterviewResultModal.jsx';

// UC-05: giai đoạn nào trên board thì cho phép bấm "Đặt lịch phỏng vấn" - tham khảo mô tả
// trong frontend_demo/uc05-schedule-interview.html ("ứng viên đang ở giai đoạn Screening
// hoặc Interview"), ánh xạ sang 3 cột gần khớp nhất trong 8 cột hiện có: đã qua vòng lọc
// hồ sơ (Shortlisted) tới lúc đã phỏng vấn xong nhưng chưa quyết định (Interviewed) - cho
// phép cả đặt thêm vòng phỏng vấn tiếp theo trong lúc còn ở Interviewed.
const INTERVIEW_ELIGIBLE_STATUSES = [
  APPLICATION_STATUS.SHORTLISTED,
  APPLICATION_STATUS.INTERVIEW_SCHEDULED,
  APPLICATION_STATUS.INTERVIEWED,
];

// UC-06 (Phase 4b): chỉ cho mở modal ghi/xem kết quả khi đã có 1 interview và nó chưa bị
// hủy - khớp requireOpenInterview phía backend (không cho ghi kết quả lên lịch CANCELLED).
// COMPLETED vẫn cho mở để xem lại (modal tự chuyển sang chế độ chỉ đọc, xem
// InterviewResultModal.jsx).
const RESULT_RECORDABLE_STATUSES = ['SCHEDULED', 'RESCHEDULED', 'COMPLETED'];

// <<boundary>> — UC-04 Update Pipeline Status + UC-07 Hire/Offer/Reject (Phase 5b: 2
// cột cuối "Offered"/"Hired" dùng lại đúng board này - xem 00_KE_HOACH_TONG_QUAN.md mục
// 2.2, "UC-07 dùng lại đúng khung state/ đã viết ở UC-04"). Bố cục tham khảo
// frontend_demo/uc04-pipeline-status.html (8 cột Kanban + form "Từ chối ứng viên" bắt
// buộc chọn lý do) và frontend_demo/uc07-hire-candidate.html (nút "Xác nhận đã tuyển" +
// lý do từ chối riêng cho giai đoạn Offer). 7 cột đầu là chuỗi trạng thái active nối
// tiếp nhau trong `state/PipelineStates.java` (SUBMITTED → ... → HIRED, advance() luôn
// đi đúng 1 bước, không cho nhảy cóc) - vì vậy board KHÔNG cho kéo thả tự do sang cột bất
// kỳ, mỗi thẻ chỉ có 1 nút "đi tiếp" sang đúng cột kế bên (ở cột Offered, đây chính là
// nước đi UC-07 "Xác nhận đã tuyển"). Cột thứ 8 (Rejected) có thể nhận thẻ từ bất kỳ cột
// active nào trừ Hired, khớp PipelineStates.RejectedState + ghi chú trong demo ("Trạng
// thái Rejected có thể đến từ bất kỳ giai đoạn nào"). WITHDRAWN không có cột riêng (không
// nằm trong 8 cột của demo, Recruiter cũng không phải người đổi trạng thái đó - ứng viên
// tự rút đơn).
const COLUMNS = [
  { status: APPLICATION_STATUS.SUBMITTED, label: 'Submitted' },
  { status: APPLICATION_STATUS.UNDER_REVIEW, label: 'Under Review' },
  { status: APPLICATION_STATUS.SHORTLISTED, label: 'Shortlisted' },
  { status: APPLICATION_STATUS.INTERVIEW_SCHEDULED, label: 'Interview Scheduled' },
  { status: APPLICATION_STATUS.INTERVIEWED, label: 'Interviewed' },
  { status: APPLICATION_STATUS.OFFERED, label: 'Offered' },
  { status: APPLICATION_STATUS.HIRED, label: 'Hired' },
  { status: APPLICATION_STATUS.REJECTED, label: 'Rejected' },
];

// Lý do từ chối gợi ý, khớp <select> trong frontend_demo/uc04-pipeline-status.html.
// "Khác" mở thêm ô nhập tay vì backend chỉ lưu 1 chuỗi `reason` tự do (không có enum
// lý do từ chối trong db.sql), không giới hạn cứng theo danh sách này.
const REJECT_REASON_PRESETS = [
  'Không đủ kinh nghiệm',
  'Không phù hợp văn hóa',
  'Ứng viên rút hồ sơ',
  'Khác',
];

// UC-07 (Phase 5b): lý do từ chối riêng cho giai đoạn Offered, khớp <select> trong
// frontend_demo/uc07-hire-candidate.html - khác với REJECT_REASON_PRESETS ở trên vì lúc
// này ứng viên đã qua hết vòng phỏng vấn, lý do từ chối mang tính chất "quyết định cuối"
// (ứng viên tự chối offer / hồ sơ không đạt ở bước cuối / vị trí đã có người khác) chứ
// không còn là lý do sàng lọc đầu vào. Cùng cơ chế reason tự do + "Khác" như trên, chỉ
// khác nội dung danh sách gợi ý.
const OFFER_REJECT_REASON_PRESETS = [
  'Ứng viên từ chối offer',
  'Không đạt yêu cầu cuối',
  'Vị trí đã có người khác',
  'Khác',
];

function formatAppliedDate(isoString) {
  if (!isoString) return '';
  return new Date(isoString).toLocaleDateString('vi-VN');
}

export function PipelineBoard() {
  const { applications, isLoading, error: loadError } = usePipelineBoard();
  const { updateStatus, isUpdating, error: updateError } = usePipelineStatus();
  // Ứng viên đang mở form từ chối (null = modal đóng). Giữ state ở board thay vì mỗi
  // pcard tự có modal riêng, vì tại 1 thời điểm chỉ có đúng 1 form từ chối đang mở.
  const [rejectTarget, setRejectTarget] = useState(null);
  // UC-05: application đang mở modal lịch phỏng vấn (null = modal đóng), cùng khuôn với
  // rejectTarget ở trên. `interviewsByApplication` (applicationId -> InterviewResponse
  // | undefined) là bộ nhớ tạm phía client cho interview mới nhất của từng application -
  // xem ghi chú GIỚI HẠN ĐÃ BIẾT trong InterviewModal.jsx: backend chưa có endpoint tra
  // interview hiện tại theo applicationId nên không nạp được từ server, chỉ cập nhật được
  // sau khi tự đặt/đổi/hủy lịch trong phiên làm việc hiện tại (mất khi tải lại trang).
  const [interviewTarget, setInterviewTarget] = useState(null);
  const [interviewsByApplication, setInterviewsByApplication] = useState({});
  // UC-06: application đang mở modal ghi/xem kết quả phỏng vấn (null = modal đóng), cùng
  // khuôn với interviewTarget ở trên - dùng chung interviewsByApplication để lấy đúng
  // interview mới nhất, không cần state riêng.
  const [resultTarget, setResultTarget] = useState(null);

  function handleAdvance(application, nextStatus) {
    updateStatus({ applicationId: application.applicationId, status: nextStatus });
  }

  function openReject(application) {
    setRejectTarget(application);
  }

  function closeReject() {
    setRejectTarget(null);
  }

  function openInterview(application) {
    setInterviewTarget(application);
  }

  function closeInterview() {
    setInterviewTarget(null);
  }

  function handleInterviewChange(applicationId, interview) {
    setInterviewsByApplication((prev) => ({ ...prev, [applicationId]: interview }));
  }

  function openResult(application) {
    setResultTarget(application);
  }

  function closeResult() {
    setResultTarget(null);
  }

  function handleResultChange(applicationId, interview) {
    setInterviewsByApplication((prev) => ({ ...prev, [applicationId]: interview }));
  }

  function confirmReject(reason) {
    if (!rejectTarget) return;
    updateStatus(
      { applicationId: rejectTarget.applicationId, status: APPLICATION_STATUS.REJECTED, reason },
      { onSuccess: closeReject },
    );
  }

  return (
    <div>
      {loadError && <div style={styles.error}>{loadError}</div>}
      {updateError && <div style={styles.error}>{updateError}</div>}

      {isLoading ? (
        <p style={styles.muted}>Đang tải...</p>
      ) : (
        <div style={styles.board}>
          {COLUMNS.map((col, colIndex) => {
            const nextCol = colIndex < COLUMNS.length - 2 ? COLUMNS[colIndex + 1] : null;
            const cards = applications.filter((a) => a.status === col.status);
            return (
              <div key={col.status} style={styles.column}>
                <h5 style={styles.columnTitle}>
                  <span>{col.label}</span>
                  <span style={styles.count}>{cards.length}</span>
                </h5>
                {cards.length === 0 && <p style={styles.emptyCol}>Không có ứng viên.</p>}
                {cards.map((app) => (
                  <div
                    key={app.applicationId}
                    style={col.status === APPLICATION_STATUS.REJECTED ? styles.cardRejected : styles.card}
                  >
                    <div style={styles.cardName}>{app.fullName}</div>
                    <div style={styles.cardMeta}>
                      {app.jobTitle}
                      {app.rating != null ? ` · ${Number(app.rating).toFixed(1)}★` : ''}
                    </div>
                    <div style={styles.cardMeta}>Nộp {formatAppliedDate(app.appliedAt)}</div>
                    {col.status !== APPLICATION_STATUS.REJECTED
                      && col.status !== APPLICATION_STATUS.HIRED && (
                        <div style={styles.cardActions}>
                          {nextCol && (
                            <Button
                              variant="primary"
                              disabled={isUpdating}
                              onClick={() => handleAdvance(app, nextCol.status)}
                            >
                              {/* UC-07: ở cột Offered, "đi tiếp" chính là quyết định
                                  tuyển - dùng đúng nhãn nút trong
                                  frontend_demo/uc07-hire-candidate.html thay vì nhãn
                                  chung "→ <cột kế tiếp>" của UC-04. */}
                              {col.status === APPLICATION_STATUS.OFFERED
                                ? 'Xác nhận đã tuyển'
                                : `→ ${nextCol.label}`}
                            </Button>
                          )}
                          <Button variant="danger" disabled={isUpdating} onClick={() => openReject(app)}>
                            Từ chối
                          </Button>
                          {INTERVIEW_ELIGIBLE_STATUSES.includes(col.status) && (
                            <Button variant="ghost" onClick={() => openInterview(app)}>
                              {interviewsByApplication[app.applicationId]
                                ? 'Xem lịch PV'
                                : 'Đặt lịch PV'}
                            </Button>
                          )}
                          {RESULT_RECORDABLE_STATUSES.includes(interviewsByApplication[app.applicationId]?.status) && (
                            <Button variant="ghost" onClick={() => openResult(app)}>
                              {interviewsByApplication[app.applicationId].status === 'COMPLETED'
                                ? 'Xem kết quả'
                                : 'Ghi kết quả'}
                            </Button>
                          )}
                        </div>
                      )}
                  </div>
                ))}
              </div>
            );
          })}
        </div>
      )}

      <RejectModal
        application={rejectTarget}
        // UC-07: từ chối ở cột Offered dùng danh sách lý do riêng
        // (OFFER_REJECT_REASON_PRESETS), các cột active còn lại giữ nguyên danh sách
        // UC-04 đã có.
        reasonPresets={
          rejectTarget?.status === APPLICATION_STATUS.OFFERED
            ? OFFER_REJECT_REASON_PRESETS
            : REJECT_REASON_PRESETS
        }
        isSubmitting={isUpdating}
        onCancel={closeReject}
        onConfirm={confirmReject}
      />

      <InterviewModal
        open={!!interviewTarget}
        application={interviewTarget}
        interview={interviewTarget ? interviewsByApplication[interviewTarget.applicationId] ?? null : null}
        onClose={closeInterview}
        onChange={(interview) => handleInterviewChange(interviewTarget.applicationId, interview)}
      />

      <InterviewResultModal
        open={!!resultTarget}
        application={resultTarget}
        interview={resultTarget ? interviewsByApplication[resultTarget.applicationId] ?? null : null}
        onClose={closeResult}
        onChange={(interview) => handleResultChange(resultTarget.applicationId, interview)}
      />
    </div>
  );
}

// Form "Từ chối ứng viên" - Alternative Flow A1/A2: bắt buộc chọn/nhập lý do trước khi
// xác nhận. Validate reason rỗng đã có sẵn ở usePipelineStatus, ở đây validate thêm để
// nút "Xác nhận từ chối" không bấm được khi rõ ràng chưa nhập gì (phản hồi sớm cho UI).
function RejectModal({ application, reasonPresets, isSubmitting, onCancel, onConfirm }) {
  // Fallback an toàn nếu component nào khác gọi RejectModal mà quên truyền reasonPresets
  // (hiện chỉ PipelineBoard gọi, nhưng giữ default để không vỡ nếu component rỗng render
  // trước khi application/reasonPresets sẵn sàng).
  const presets = reasonPresets ?? REJECT_REASON_PRESETS;
  const [preset, setPreset] = useState(presets[0]);
  const [customReason, setCustomReason] = useState('');

  // Reset form mỗi khi mở modal cho MỘT application khác (kể cả khi presets đổi giữa
  // giai đoạn UC-04 và UC-07, vd đóng modal Offered rồi mở lại ở Submitted) - modal này
  // không unmount giữa các lần mở nên state cũ có thể còn sót lại. Sửa luôn 1 bug có từ
  // trước (Phase 2b): `confirmReject` đóng modal qua `closeReject` sau khi submit thành
  // công, không đi qua `handleClose`, nên preset/customReason không được reset - trước
  // đây không lộ ra vì chỉ có 1 danh sách preset dùng chung, giờ có 2 danh sách khác nhau
  // theo giai đoạn nên cần reset đúng lúc application đổi, không phụ thuộc đường đóng
  // modal nào được gọi.
  useEffect(() => {
    setPreset(presets[0]);
    setCustomReason('');
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [application?.applicationId]);

  if (!application) return null;

  const isCustom = preset === 'Khác';
  const reason = isCustom ? customReason.trim() : preset;
  const canConfirm = reason.length > 0 && !isSubmitting;

  function handleClose() {
    onCancel();
  }

  return (
    <Modal open onClose={handleClose} title="Từ chối ứng viên">
      <p style={styles.muted}>
        Ứng viên: <strong>{application.fullName}</strong>
      </p>
      <div style={styles.field}>
        <label style={styles.label}>
          Lý do từ chối <span style={{ color: '#ef4444' }}>*</span>
        </label>
        <select
          style={styles.select}
          value={preset}
          onChange={(e) => setPreset(e.target.value)}
        >
          {presets.map((r) => (
            <option key={r} value={r}>{r}</option>
          ))}
        </select>
      </div>
      {isCustom && (
        <div style={styles.field}>
          <textarea
            style={styles.textarea}
            placeholder="Nhập lý do..."
            value={customReason}
            onChange={(e) => setCustomReason(e.target.value)}
          />
        </div>
      )}
      <div style={styles.modalActions}>
        <Button variant="ghost" onClick={handleClose}>Hủy</Button>
        <Button variant="danger" disabled={!canConfirm} onClick={() => onConfirm(reason)}>
          Xác nhận từ chối
        </Button>
      </div>
    </Modal>
  );
}

const styles = {
  board: { display: 'flex', gap: 12, overflowX: 'auto', paddingBottom: 8 },
  column: {
    minWidth: 220,
    flex: '0 0 220px',
    background: '#f7faf9',
    border: '1px solid #e5e7eb',
    borderRadius: 12,
    padding: 10,
  },
  columnTitle: {
    margin: '2px 4px 10px',
    fontSize: 12,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    textTransform: 'uppercase',
    letterSpacing: 0.3,
    color: '#0f172a',
  },
  count: {
    background: '#fff',
    border: '1px solid #e5e7eb',
    borderRadius: 999,
    padding: '1px 8px',
    fontSize: 11,
    color: '#475569',
  },
  card: {
    background: '#fff',
    border: '1px solid #e5e7eb',
    borderRadius: 10,
    padding: '10px 11px',
    marginBottom: 9,
    boxShadow: '0 2px 6px rgba(6,44,34,.05)',
  },
  cardRejected: {
    background: '#fff',
    border: '1px solid #e5e7eb',
    borderRadius: 10,
    padding: '10px 11px',
    marginBottom: 9,
    opacity: 0.75,
  },
  cardName: { fontWeight: 700, fontSize: 13, color: '#0f172a' },
  cardMeta: { color: '#6b7280', fontSize: 11, marginTop: 3 },
  cardActions: { display: 'flex', gap: 6, marginTop: 8, flexWrap: 'wrap' },
  emptyCol: { color: '#9ca3af', fontSize: 12, padding: '8px 2px' },
  muted: { color: '#6b7280', fontSize: 14 },
  error: {
    background: '#fee2e2',
    color: '#991b1b',
    padding: '10px 12px',
    borderRadius: 6,
    marginBottom: 12,
    fontSize: 14,
  },
  field: { marginBottom: 14 },
  label: { display: 'block', margin: '0 0 6px', fontWeight: 700, fontSize: 13, color: '#111827' },
  select: {
    width: '100%',
    padding: '8px 12px',
    borderRadius: 6,
    border: '1px solid #d1d5db',
  },
  textarea: {
    width: '100%',
    minHeight: 70,
    padding: '8px 12px',
    borderRadius: 6,
    border: '1px solid #d1d5db',
    resize: 'vertical',
    font: 'inherit',
  },
  modalActions: { display: 'flex', gap: 10, justifyContent: 'flex-end' },
};