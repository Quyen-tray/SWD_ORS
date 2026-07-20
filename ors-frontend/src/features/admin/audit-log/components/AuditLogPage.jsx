import { useAuditLog } from '../hooks/useAuditLog.js';
import { Table } from '../../../../shared/components/Table.jsx';
import { formatDate } from '../../../../shared/utils/formatDate.js';
import { AUDIT_ACTION_LABEL } from '../../../../shared/utils/auditActionLabels.js';

// <<boundary>> — màn hình xem nhật ký kiểm toán (UC-61). Hiện thực hoá NFR-FE07-2:
// mọi thao tác quản trị đều phải xem lại được. Chỉ đọc, không có hành động ghi.
export function AuditLogPage() {
  const { logs, isLoading, keyword, setKeyword } = useAuditLog();

  const columns = [
    { key: 'createdAt', label: 'Thời gian', render: (l) => formatDate(l.createdAt) || '—' },
    { key: 'userEmail', label: 'Admin thực hiện' },
    { key: 'actionType', label: 'Hành động', render: (l) => AUDIT_ACTION_LABEL[l.actionType] ?? l.actionType },
    { key: 'description', label: 'Mô tả' },
  ];

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Nhật ký kiểm toán</h2>

      <div style={styles.card}>
        <div style={styles.toolbar}>
          <input
            style={styles.input}
            placeholder="Tìm theo admin hoặc hành động..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>

        <p style={styles.muted}>
          {isLoading ? 'Đang tải...' : `Hiển thị ${logs.length} bản ghi`}
        </p>

        {!isLoading && (
          <Table columns={columns} data={logs} emptyMessage="Chưa có bản ghi nào." />
        )}
      </div>
    </div>
  );
}

const styles = {
  card: { background: '#fff', borderRadius: 10, padding: 20, border: '1px solid #e5e7eb' },
  toolbar: { display: 'flex', gap: 8, alignItems: 'center', marginBottom: 12 },
  input: { padding: '8px 12px', borderRadius: 6, border: '1px solid #d1d5db', minWidth: 320 },
  muted: { color: '#6b7280', fontSize: 14 },
};
