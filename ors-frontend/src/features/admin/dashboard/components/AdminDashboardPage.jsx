import { Link } from 'react-router-dom';
import { useAdminDashboard } from '../hooks/useAdminDashboard.js';

const STATUS_LABEL = { ACTIVE: 'Đang hoạt động', INACTIVE: 'Tạm ngưng', BANNED: 'Bị cấm', EMAIL_PENDING: 'Chờ xác thực', LOCKED: 'Bị khoá' };

// <<boundary>> — trang tổng quan. Thuần UI, KHÔNG phải một UC riêng: chỉ tổng hợp lại
// dữ liệu của UC-53 (User Management) và UC-58 (Job Category Management).
export function AdminDashboardPage() {
  const { isLoading, totalUsers, totalCategories, usersByStatus, usersByRole } = useAdminDashboard();

  if (isLoading) return <p>Đang tải...</p>;

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Tổng quan</h2>

      <div style={styles.grid}>
        <Link to="/admin/users" style={styles.cardLink}>
          <div style={styles.card}>
            <div style={styles.cardNumber}>{totalUsers}</div>
            <div style={styles.cardLabel}>Người dùng</div>
          </div>
        </Link>
        <Link to="/admin/job-categories" style={styles.cardLink}>
          <div style={styles.card}>
            <div style={styles.cardNumber}>{totalCategories}</div>
            <div style={styles.cardLabel}>Danh mục ngành nghề</div>
          </div>
        </Link>
        <Link to="/admin/audit-logs" style={styles.cardLink}>
          <div style={styles.card}>
            <div style={styles.cardNumber}>&rarr;</div>
            <div style={styles.cardLabel}>Xem nhật ký kiểm toán</div>
          </div>
        </Link>
      </div>

      <div style={styles.row}>
        <div style={styles.card}>
          <h3 style={styles.sectionTitle}>Người dùng theo trạng thái</h3>
          {Object.entries(usersByStatus).map(([status, count]) => (
            <div key={status} style={styles.breakdownRow}>
              <span>{STATUS_LABEL[status] ?? status}</span>
              <strong>{count}</strong>
            </div>
          ))}
        </div>
        <div style={styles.card}>
          <h3 style={styles.sectionTitle}>Người dùng theo vai trò</h3>
          {Object.entries(usersByRole).map(([role, count]) => (
            <div key={role} style={styles.breakdownRow}>
              <span>{role}</span>
              <strong>{count}</strong>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

const styles = {
  grid: { display: 'flex', gap: 16, marginBottom: 24, flexWrap: 'wrap' },
  cardLink: { textDecoration: 'none', color: 'inherit', flex: '1 1 200px' },
  card: { background: '#fff', borderRadius: 10, padding: 20, border: '1px solid #e5e7eb' },
  cardNumber: { fontSize: 32, fontWeight: 700, color: '#111827' },
  cardLabel: { color: '#6b7280', fontSize: 14, marginTop: 4 },
  row: { display: 'flex', gap: 16, flexWrap: 'wrap' },
  sectionTitle: { marginTop: 0, fontSize: 16 },
  breakdownRow: { display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f1f5f9' },
};
