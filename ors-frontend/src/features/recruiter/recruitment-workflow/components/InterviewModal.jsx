import { useEffect, useState } from 'react';
import { Modal } from '../../../../shared/components/Modal.jsx';
import { Button } from '../../../../shared/components/Button.jsx';
import { useInterviewSchedule } from '../hooks/useInterviewSchedule.js';

const ACTIVE_STATUSES = ['SCHEDULED', 'RESCHEDULED'];

function pad(n) {
  return String(n).padStart(2, '0');
}
function toDateInputValue(iso) {
  if (!iso) return '';
  const d = new Date(iso);
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
}
function toTimeInputValue(iso) {
  if (!iso) return '';
  const d = new Date(iso);
  return `${pad(d.getHours())}:${pad(d.getMinutes())}`;
}
// Backend yêu cầu scheduledTime là ISO-8601 CÓ offset/"Z" (xem ghi chú trong
// ScheduleInterviewRequest.java, Phase 3a) - giá trị thô của <input type="date">/"time">
// (vd "2026-07-20", "14:30") không có offset, Jackson sẽ parse lỗi 400 trước khi vào tới
// service. new Date(`${date}T${time}`) parse theo giờ local trình duyệt, .toISOString()
// tự quy đổi sang UTC kèm "Z" đúng định dạng backend cần.
function combineToISOString(date, time) {
  if (!date || !time) return null;
  return new Date(`${date}T${time}`).toISOString();
}
function formatVN(iso) {
  if (!iso) return '';
  return new Date(iso).toLocaleString('vi-VN');
}

// <<boundary>> — UC-05 Schedule/Reschedule/Cancel Interview. Bố cục tham khảo
// frontend_demo/uc05-schedule-interview.html, nhưng bỏ 2 field trong demo không có cột
// tương ứng ở backend (xem InterviewResponse/ScheduleInterviewRequest, Phase 3a):
// "Người phỏng vấn" (không có cột nào trong bảng interviews) và "Ghi chú cho ứng viên"
// (comments đã dùng cho UC-06 - kết quả phỏng vấn, không phải ghi chú lúc đặt lịch, để
// tránh đá nhau 2 use case dùng chung 1 cột). Chip "Hình thức phỏng vấn" trong demo cũng
// bỏ luôn - bản demo tĩnh không có JS đứng sau chip đó (`<script>` rỗng) và cũng không có
// cột lưu, chỉ còn `location`/`meetingLink` là 2 field thật sự được lưu.
//
// `interview` (prop, do PipelineBoard truyền vào) = null: chưa có lịch cho application này
// -> form đặt lịch mới (POST /interviews).
// `interview.status` SCHEDULED/RESCHEDULED: đang có lịch hiệu lực -> chỉ cho sửa ngày/giờ
// (POST /interviews/{id}/reschedule chỉ nhận scheduledTime, không có endpoint sửa riêng
// location/meetingLink) + nút Hủy lịch.
// `interview.status` CANCELLED: lịch cũ đã hủy -> cho đặt lịch MỚI, dùng lại đúng form ban
// đầu (round tự tăng ở InterviewService.scheduleInterview, không cần tự tính ở FE).
// `interview.status` COMPLETED: chỉ xem, không có hành động (UC-06 ghi outcome nằm ở
// Phase 4a/4b, chưa có đường dẫn nào set COMPLETED ở phase này).
//
// GHI CHÚ QUAN TRỌNG (giới hạn đã biết, chưa có cách khắc phục trong phạm vi Phase 3b):
// backend hiện KHÔNG có endpoint "lấy lịch phỏng vấn hiện tại theo applicationId" (chỉ có
// GET /interviews/{id} theo id của chính interview) và CandidateSummaryResponse (UC-01)
// cũng không kèm thông tin interview. Vì Phase 3b chỉ được đụng frontend (xem quy ước
// trong 00_PROGRESS.md), `interview` cho mỗi application đang được PipelineBoard giữ tạm
// trong React state (mất khi tải lại trang) thay vì nạp từ server. Nếu Phase 6 (review) hoặc
// 1 phase sau có thể sửa backend, nên thêm field kiểu `latestInterview` vào
// CandidateSummaryResponse hoặc 1 endpoint GET /recruiter/interviews?applicationId= để bỏ
// hẳn cách giữ tạm này.
export function InterviewModal({ open, application, interview, onClose, onChange }) {
  const {
    scheduleInterview, isScheduling, scheduleError,
    cancelInterview, isCancelling, cancelError,
    rescheduleInterview, isRescheduling, rescheduleError,
  } = useInterviewSchedule();

  const isActive = !!interview && ACTIVE_STATUSES.includes(interview.status);
  const isCancelled = interview?.status === 'CANCELLED';
  const isCompleted = interview?.status === 'COMPLETED';
  const isNewSchedule = !interview || isCancelled;

  const [date, setDate] = useState('');
  const [time, setTime] = useState('');
  const [location, setLocation] = useState('');
  const [meetingLink, setMeetingLink] = useState('');

  // Nạp lại form mỗi khi mở modal cho 1 application/interview khác, hoặc sau khi trạng
  // thái interview đổi (vd vừa hủy xong -> chuyển sang form đặt lịch mới trống).
  useEffect(() => {
    if (!open) return;
    if (isActive) {
      setDate(toDateInputValue(interview.scheduledTime));
      setTime(toTimeInputValue(interview.scheduledTime));
    } else {
      setDate('');
      setTime('');
      setLocation('');
      setMeetingLink('');
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open, interview?.id, interview?.status]);

  if (!open || !application) return null;

  const isSubmitting = isScheduling || isCancelling || isRescheduling;
  const errorMessage = scheduleError || cancelError || rescheduleError;
  const canSubmit = !!date && !!time && !isSubmitting;

  function handleClose() {
    if (isSubmitting) return;
    onClose();
  }

  function handleSchedule() {
    const scheduledTime = combineToISOString(date, time);
    if (!scheduledTime) return;
    scheduleInterview(
      {
        applicationId: application.applicationId,
        scheduledTime,
        location: location.trim() || null,
        meetingLink: meetingLink.trim() || null,
      },
      { onSuccess: (data) => onChange(data) },
    );
  }

  function handleReschedule() {
    const scheduledTime = combineToISOString(date, time);
    if (!scheduledTime) return;
    rescheduleInterview(
      { interviewId: interview.id, newTime: scheduledTime },
      { onSuccess: (data) => onChange(data) },
    );
  }

  function handleCancel() {
    cancelInterview(interview.id, { onSuccess: (data) => onChange(data) });
  }

  const title = isNewSchedule ? 'Đặt lịch phỏng vấn' : 'Lịch phỏng vấn';

  return (
    <Modal open={open} onClose={handleClose} title={title}>
      <div style={styles.field}>
        <label style={styles.label}>Ứng viên</label>
        <div>
          <div style={styles.name}>{application.fullName}</div>
          {application.jobTitle && <div style={styles.muted}>{application.jobTitle}</div>}
        </div>
      </div>

      {errorMessage && <div style={styles.error}>{errorMessage}</div>}

      {isCancelled && (
        <div style={styles.notice}>
          Lịch phỏng vấn vòng {interview.round} đã bị hủy. Đặt lịch mới bên dưới nếu cần.
        </div>
      )}

      {isCompleted ? (
        <div style={styles.notice}>
          Vòng {interview.round} đã hoàn thành lúc {formatVN(interview.scheduledTime)}.
        </div>
      ) : (
        <>
          <div style={styles.formRow}>
            <div style={styles.field}>
              <label style={styles.label}>Ngày <span style={styles.req}>*</span></label>
              <input
                style={styles.input}
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
              />
            </div>
            <div style={styles.field}>
              <label style={styles.label}>Giờ <span style={styles.req}>*</span></label>
              <input
                style={styles.input}
                type="time"
                value={time}
                onChange={(e) => setTime(e.target.value)}
              />
            </div>
          </div>

          {isActive ? (
            <div style={styles.field}>
              <label style={styles.label}>Địa điểm / Link phòng họp</label>
              <div style={styles.muted}>
                {interview.location || interview.meetingLink || 'Chưa có thông tin'}
                {' — không sửa được khi đổi lịch, chỉ đổi được ngày giờ.'}
              </div>
            </div>
          ) : (
            <>
              <div style={styles.field}>
                <label style={styles.label}>Địa điểm</label>
                <input
                  style={styles.input}
                  value={location}
                  placeholder="VD: Văn phòng công ty, tầng 5"
                  onChange={(e) => setLocation(e.target.value)}
                />
              </div>
              <div style={styles.field}>
                <label style={styles.label}>Link phòng họp</label>
                <input
                  style={styles.input}
                  value={meetingLink}
                  placeholder="https://meet.google.com/..."
                  onChange={(e) => setMeetingLink(e.target.value)}
                />
              </div>
            </>
          )}
        </>
      )}

      <div style={styles.actions}>
        {isActive && (
          <Button variant="danger" disabled={isSubmitting} onClick={handleCancel}>
            {isCancelling ? 'Đang hủy...' : 'Hủy lịch'}
          </Button>
        )}
        <div style={{ flex: 1 }} />
        <Button variant="ghost" disabled={isSubmitting} onClick={handleClose}>Đóng</Button>
        {!isCompleted && (
          <Button
            variant="primary"
            disabled={!canSubmit}
            onClick={isActive ? handleReschedule : handleSchedule}
          >
            {isActive
              ? (isRescheduling ? 'Đang lưu...' : 'Lưu thay đổi')
              : (isScheduling ? 'Đang gửi...' : 'Gửi lời mời')}
          </Button>
        )}
      </div>
    </Modal>
  );
}

const styles = {
  field: { marginBottom: 14 },
  formRow: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 },
  label: { display: 'block', margin: '0 0 6px', fontWeight: 700, fontSize: 13, color: '#111827' },
  req: { color: '#ef4444' },
  name: { fontWeight: 800, fontSize: 14, color: '#0f172a' },
  muted: { color: '#6b7280', fontSize: 12.5 },
  input: {
    width: '100%',
    height: 38,
    padding: '0 12px',
    borderRadius: 8,
    border: '1px solid #d1d5db',
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