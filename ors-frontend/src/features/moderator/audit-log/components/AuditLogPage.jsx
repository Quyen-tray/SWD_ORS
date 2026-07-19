import { useMemo } from 'react';
import { Pagination } from '../../../../shared/components/Pagination.jsx';
import { usePagination } from '../../../../shared/hooks/usePagination.js';
import { useAuditLog } from '../hooks/useAuditLog.js';
import { Card, PageHeader, ActionBadge, EmptyState, LoadingBlock, ErrorBanner } from '../../shared/ui.jsx';

const selectStyle = {
  padding: '8px 12px',
  borderRadius: 8,
  border: '1px solid #e2e8f0',
  background: '#fff',
  fontSize: 14,
  color: '#334155',
};

// <<boundary>> — UC-50. entityType/entityId có thể trống nếu dòng audit không gắn với
// entity cụ thể nào (vd DASHBOARD_ACCESS, REPORT_QUEUE_ACCESS).
export function AuditLogPage() {
  const {
    entries, isLoading, error,
    entityType, setEntityType,
    entityId, setEntityId,
    actionType, setActionType,
    mineOnly, setMineOnly,
  } = useAuditLog();
  const { page, pageSize, setPage } = usePagination();

  const totalPages = Math.max(1, Math.ceil(entries.length / pageSize));
  const pageItems = useMemo(
    () => entries.slice((page - 1) * pageSize, page * pageSize),
    [entries, page, pageSize],
  );

  return (
    <div>
      <PageHeader icon="🗂️" title="Audit Log" subtitle={`${entries.length} bản ghi - nhật ký bất biến mọi hành động kiểm duyệt (BR-10).`} />

      <div style={{ display: 'flex', gap: 12, marginBottom: 16, flexWrap: 'wrap', alignItems: 'center' }}>
        <select style={selectStyle} value={entityType} onChange={(e) => { setEntityType(e.target.value); setPage(1); }}>
          <option value="">Tất cả entity type</option>
          <option value="REPORT">REPORT</option>
          <option value="JOB_POST">JOB_POST</option>
          <option value="COMPANY">COMPANY</option>
        </select>
        <input
          placeholder="Entity ID"
          value={entityId}
          onChange={(e) => { setEntityId(e.target.value); setPage(1); }}
          style={{ ...selectStyle, width: 100 }}
        />
        <input
          placeholder="Action Type"
          value={actionType}
          onChange={(e) => { setActionType(e.target.value); setPage(1); }}
          style={selectStyle}
        />
        <label style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 14, color: '#334155' }}>
          <input type="checkbox" checked={mineOnly} onChange={(e) => { setMineOnly(e.target.checked); setPage(1); }} />
          Chỉ hành động của tôi
        </label>
      </div>

      <ErrorBanner message={error} />

      <Card style={{ overflow: 'hidden' }}>
        {isLoading ? (
          <LoadingBlock />
        ) : pageItems.length === 0 ? (
          <EmptyState icon="🗂️" message="Không có audit log nào khớp bộ lọc." />
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ background: '#f8fafc' }}>
                {['Timestamp', 'Moderator', 'Action Type', 'Entity', 'Description'].map((h) => (
                  <th key={h} style={{ textAlign: 'left', padding: '12px 16px', fontSize: 12, color: '#64748b', fontWeight: 700, textTransform: 'uppercase', letterSpacing: 0.4 }}>
                    {h}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {pageItems.map((row) => (
                <tr key={row.id} className="mod-table-row" style={{ borderTop: '1px solid #f1f5f9' }}>
                  <td style={{ padding: '12px 16px', color: '#475569', fontSize: 13, whiteSpace: 'nowrap' }}>
                    {new Date(row.createdAt).toLocaleString('vi-VN')}
                  </td>
                  <td style={{ padding: '12px 16px', color: '#0f172a' }}>#{row.moderatorId}</td>
                  <td style={{ padding: '12px 16px' }}>
                    <ActionBadge action={row.actionType} />
                  </td>
                  <td style={{ padding: '12px 16px', fontSize: 13, color: '#475569', whiteSpace: 'nowrap' }}>
                    {row.entityType && row.entityId != null ? `${row.entityType} #${row.entityId}` : (row.entityType ?? '-')}
                  </td>
                  <td style={{ padding: '12px 16px', fontSize: 13, color: '#334155' }}>{row.description}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Card>

      <Pagination page={page} totalPages={totalPages} onChange={setPage} />
    </div>
  );
}
