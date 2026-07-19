import { useCandidateList } from '../hooks/useCandidateList.js';
import { Table } from '../../../../shared/components/Table.jsx';
import { Pagination } from '../../../../shared/components/Pagination.jsx';
import { Button } from '../../../../shared/components/Button.jsx';
import { APPLICATION_STATUS } from '../../../../shared/types/index.js';

// Nhãn tiếng Việt cho 9 giá trị job_applications.status (khớp CandidateCriteria bên
// backend). WITHDRAWN vẫn hiện trong bộ lọc dù UC-01 demo gốc không có, vì backend
// chấp nhận giá trị này (ứng viên tự rút đơn) và ẩn đi sẽ khiến recruiter không lọc
// được nhóm ứng viên đó.
const STATUS_LABEL = {
  [APPLICATION_STATUS.SUBMITTED]: 'Đã nộp',
  [APPLICATION_STATUS.UNDER_REVIEW]: 'Đang xem xét',
  [APPLICATION_STATUS.SHORTLISTED]: 'Vào danh sách rút gọn',
  [APPLICATION_STATUS.INTERVIEW_SCHEDULED]: 'Đã lên lịch phỏng vấn',
  [APPLICATION_STATUS.INTERVIEWED]: 'Đã phỏng vấn',
  [APPLICATION_STATUS.OFFERED]: 'Đã đề nghị',
  [APPLICATION_STATUS.HIRED]: 'Đã tuyển',
  [APPLICATION_STATUS.REJECTED]: 'Bị từ chối',
  [APPLICATION_STATUS.WITHDRAWN]: 'Đã rút đơn',
};

// Nhóm màu theo giai đoạn pipeline, phỏng theo .badge-* trong frontend_demo/uc01-candidate-list.html
// (pending/screening/interview/offer/hired/rejected) nhưng viết bằng inline style để khớp
// convention của phần code đã có (UserManagementPage, không dùng file .css riêng).
const STATUS_COLOR = {
  [APPLICATION_STATUS.SUBMITTED]: { background: '#eef2f7', color: '#475569' },
  [APPLICATION_STATUS.UNDER_REVIEW]: { background: '#dbeafe', color: '#1d4ed8' },
  [APPLICATION_STATUS.SHORTLISTED]: { background: '#dbeafe', color: '#1d4ed8' },
  [APPLICATION_STATUS.INTERVIEW_SCHEDULED]: { background: '#fef3c7', color: '#b45309' },
  [APPLICATION_STATUS.INTERVIEWED]: { background: '#fef3c7', color: '#b45309' },
  [APPLICATION_STATUS.OFFERED]: { background: '#ede9fe', color: '#6d28d9' },
  [APPLICATION_STATUS.HIRED]: { background: '#dcfce7', color: '#166534' },
  [APPLICATION_STATUS.REJECTED]: { background: '#fee2e2', color: '#991b1b' },
  [APPLICATION_STATUS.WITHDRAWN]: { background: '#f3f4f6', color: '#4b5563' },
};

function statusBadge(status) {
  const colors = STATUS_COLOR[status] ?? STATUS_COLOR[APPLICATION_STATUS.SUBMITTED];
  return { ...pillBase, ...colors };
}

// Đánh giá là BigDecimal 0..5 phía backend, có thể null nếu recruiter chưa chấm.
function stars(rating) {
  if (rating == null) return <span style={styles.muted}>Chưa đánh giá</span>;
  const value = Math.round(Number(rating));
  const filled = '★'.repeat(value);
  const empty = '☆'.repeat(Math.max(0, 5 - value));
  return (
    <span>
      <span style={{ color: '#f59e0b', letterSpacing: 1 }}>{filled}</span>
      <span style={{ color: '#d1d5db', letterSpacing: 1 }}>{empty}</span>
      <span style={styles.muted}> {Number(rating).toFixed(1)}</span>
    </span>
  );
}

function formatAppliedDate(isoString) {
  if (!isoString) return '';
  return new Date(isoString).toLocaleDateString('vi-VN');
}

// <<boundary>> — UC-01 View Candidate List. Tham khảo bố cục từ
// frontend_demo/uc01-candidate-list.html (toolbar tìm kiếm + lọc, bảng ứng viên với
// cột đánh giá dạng sao và badge trạng thái); các cột "Hồ sơ"/"Nhắn tin" trong bản demo
// (UC-02, UC-08 - ngoài scope đồ án) không đưa vào đây.
export function CandidateManagementPage() {
  const {
    candidates, total, totalPages, isLoading, error,
    keyword, setKeyword, status, setStatus, resetFilters,
    page, setPage,
  } = useCandidateList();

  const columns = [
    { key: 'fullName', label: 'Ứng viên' },
    { key: 'jobTitle', label: 'Job ứng tuyển' },
    {
      key: 'cv',
      label: 'CV',
      render: (c) => c.cvName ?? <span style={styles.muted}>Chưa có CV</span>,
    },
    { key: 'rating', label: 'Đánh giá', render: (c) => stars(c.rating) },
    {
      key: 'status',
      label: 'Trạng thái',
      render: (c) => <span style={statusBadge(c.status)}>{STATUS_LABEL[c.status] ?? c.status}</span>,
    },
    { key: 'appliedAt', label: 'Ngày nộp', render: (c) => formatAppliedDate(c.appliedAt) },
  ];

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Danh sách ứng viên</h2>

      <div style={styles.card}>
        <div style={styles.toolbar}>
          <input
            style={styles.input}
            placeholder="Tìm theo tên hoặc email..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <select style={styles.select} value={status} onChange={(e) => setStatus(e.target.value)}>
            <option value="">Trạng thái: Tất cả</option>
            {Object.values(APPLICATION_STATUS).map((s) => (
              <option key={s} value={s}>{STATUS_LABEL[s]}</option>
            ))}
          </select>
          <Button variant="ghost" onClick={resetFilters}>Đặt lại</Button>
        </div>

        {error && <div style={styles.error}>{error}</div>}

        <p style={styles.muted}>
          {isLoading ? 'Đang tải...' : `Hiển thị ${candidates.length} / ${total} ứng viên`}
        </p>

        {!isLoading && (
          <Table columns={columns} data={candidates.map((c) => ({ ...c, id: c.applicationId }))}
            emptyMessage="Không tìm thấy ứng viên nào." />
        )}

        <Pagination page={page} totalPages={totalPages} onChange={setPage} />
      </div>
    </div>
  );
}

const pillBase = {
  display: 'inline-block',
  padding: '2px 10px',
  borderRadius: 999,
  fontSize: 12,
  fontWeight: 600,
  whiteSpace: 'nowrap',
};

const styles = {
  card: { background: '#fff', borderRadius: 10, padding: 20, border: '1px solid #e5e7eb' },
  toolbar: { display: 'flex', gap: 8, alignItems: 'center', flexWrap: 'wrap', marginBottom: 12 },
  input: { padding: '8px 12px', borderRadius: 6, border: '1px solid #d1d5db', minWidth: 260 },
  select: { padding: '8px 12px', borderRadius: 6, border: '1px solid #d1d5db' },
  muted: { color: '#6b7280', fontSize: 14 },
  error: {
    background: '#fee2e2',
    color: '#991b1b',
    padding: '10px 12px',
    borderRadius: 6,
    marginBottom: 12,
    fontSize: 14,
  },
};