import { Link, useParams } from 'react-router-dom';
import { useUserDetail } from '../hooks/useUserDetail.js';
import { Table } from '../../../../shared/components/Table.jsx';
import { formatDate } from '../../../../shared/utils/formatDate.js';
import { AUDIT_ACTION_LABEL } from '../../../../shared/utils/auditActionLabels.js';

const STATUS_LABEL = {
  ACTIVE: 'Đang hoạt động',
  INACTIVE: 'Tạm ngưng',
  BANNED: 'Bị cấm',
  EMAIL_PENDING: 'Chờ xác thực',
  LOCKED: 'Bị khoá',
};

const STATUS_COLOR = {
  ACTIVE: { background: '#dcfce7', color: '#166534' },
  INACTIVE: { background: '#fef3c7', color: '#92400e' },
  BANNED: { background: '#fee2e2', color: '#991b1b' },
  EMAIL_PENDING: { background: '#e0e7ff', color: '#3730a3' },
  LOCKED: { background: '#e5e7eb', color: '#374151' },
};

const pillBase = { display: 'inline-block', padding: '2px 10px', borderRadius: 999, fontSize: 12, fontWeight: 600 };

function statusPill(status) {
  return { ...pillBase, ...(STATUS_COLOR[status] ?? STATUS_COLOR.LOCKED) };
}

// <<boundary>> — màn hình xem chi tiết một người dùng (UC-62): hồ sơ + lịch sử thao tác.
// Chỉ đọc, không có hành động ghi (activate/deactivate/ban vẫn thực hiện ở UserManagementPage).
export function UserDetailPage() {
  const { id } = useParams();
  const { user, history, isLoading, notFound } = useUserDetail(id);

  const historyColumns = [
    { key: 'createdAt', label: 'Thời gian', render: (l) => formatDate(l.createdAt) || '—' },
    { key: 'actionType', label: 'Hành động', render: (l) => AUDIT_ACTION_LABEL[l.actionType] ?? l.actionType },
    { key: 'description', label: 'Mô tả' },
  ];

  return (
    <div>
      <Link to="/admin/users" style={styles.back}>&larr; Về danh sách người dùng</Link>
      <h2 style={{ marginTop: 8 }}>Chi tiết người dùng</h2>

      {isLoading && <p>Đang tải...</p>}
      {notFound && <p style={styles.error}>Không tìm thấy người dùng này.</p>}

      {user && (
        <>
          <div style={styles.card}>
            <div style={styles.field}><span style={styles.muted}>Email</span><strong>{user.email}</strong></div>
            <div style={styles.field}><span style={styles.muted}>Vai trò</span><span style={pillBase}>{user.role}</span></div>
            <div style={styles.field}><span style={styles.muted}>Trạng thái</span><span style={statusPill(user.status)}>{STATUS_LABEL[user.status] ?? user.status}</span></div>
            <div style={styles.field}><span style={styles.muted}>Ngày tạo</span><span>{formatDate(user.createdAt)}</span></div>
            <div style={styles.field}><span style={styles.muted}>Cập nhật lần cuối</span><span>{formatDate(user.updatedAt)}</span></div>
          </div>

          <h3>Lịch sử thao tác</h3>
          <div style={styles.card}>
            <Table columns={historyColumns} data={history} emptyMessage="Chưa có thao tác nào trên tài khoản này." />
          </div>
        </>
      )}
    </div>
  );
}

const styles = {
  back: { color: '#2563eb', textDecoration: 'none', fontSize: 14 },
  card: { background: '#fff', borderRadius: 10, padding: 20, border: '1px solid #e5e7eb', marginBottom: 16 },
  field: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '8px 0', borderBottom: '1px solid #f1f5f9' },
  muted: { color: '#6b7280', fontSize: 14 },
  error: { background: '#fee2e2', color: '#991b1b', padding: '10px 12px', borderRadius: 6, fontSize: 14 },
};
