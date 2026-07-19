import { useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { Pagination } from '../../../../shared/components/Pagination.jsx';
import { usePagination } from '../../../../shared/hooks/usePagination.js';
import { REPORT_STATUS } from '../../../../shared/types/index.js';
import { useReportQueue } from '../hooks/useReportQueue.js';
import { Card, PageHeader, StatusBadge, SlaBadge, EmptyState, LoadingBlock, ErrorBanner } from '../../shared/ui.jsx';

// <<boundary>> — UC-45: danh sách report, lọc theo status + sắp xếp theo ngày tạo.
const selectStyle = {
  padding: '8px 12px',
  borderRadius: 8,
  border: '1px solid #e2e8f0',
  background: '#fff',
  fontSize: 14,
  color: '#334155',
};

export function ReportQueuePage() {
  const { reports, isLoading, error, status, setStatus, sort, setSort } = useReportQueue();
  const { page, pageSize, setPage } = usePagination();
  const navigate = useNavigate();

  const totalPages = Math.max(1, Math.ceil(reports.length / pageSize));
  const pageItems = useMemo(
    () => reports.slice((page - 1) * pageSize, page * pageSize),
    [reports, page, pageSize],
  );

  return (
    <div>
      <PageHeader
        icon="🚩"
        title="Report Queue"
        subtitle={`${reports.length} report — lọc theo trạng thái, sắp xếp theo ngày tạo.`}
      />

      <div style={{ display: 'flex', gap: 12, marginBottom: 16 }}>
        <select
          style={selectStyle}
          value={status}
          onChange={(e) => {
            setStatus(e.target.value);
            setPage(1);
          }}
        >
          <option value="">Tất cả trạng thái</option>
          {Object.values(REPORT_STATUS).map((s) => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
        <select style={selectStyle} value={sort} onChange={(e) => setSort(e.target.value)}>
          <option value="date_desc">Mới nhất trước</option>
          <option value="date_asc">Cũ nhất trước</option>
        </select>
      </div>

      <ErrorBanner message={error} />

      <Card style={{ overflow: 'hidden' }}>
        {isLoading ? (
          <LoadingBlock />
        ) : pageItems.length === 0 ? (
          <EmptyState icon="🚩" message="Không có report nào khớp bộ lọc." />
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ background: '#f8fafc' }}>
                {['ID', 'Job Post', 'Reporter', 'Created At', 'Status', ''].map((h) => (
                  <th key={h} style={{ textAlign: 'left', padding: '12px 16px', fontSize: 12, color: '#64748b', fontWeight: 700, textTransform: 'uppercase', letterSpacing: 0.4 }}>
                    {h}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {pageItems.map((row) => (
                <tr
                  key={row.id}
                  className="mod-table-row"
                  style={{ borderTop: '1px solid #f1f5f9', cursor: 'pointer' }}
                  onClick={() => navigate(`/moderator/reports/${row.id}`)}
                >
                  <td style={{ padding: '12px 16px', color: '#94a3b8', fontSize: 13 }}>#{row.id}</td>
                  <td style={{ padding: '12px 16px', fontWeight: 600, color: '#0f172a' }}>{row.jobPostTitle}</td>
                  <td style={{ padding: '12px 16px', color: '#475569' }}>{row.reporterEmail}</td>
                  <td style={{ padding: '12px 16px', color: '#475569', fontSize: 13 }}>
                    {new Date(row.createdAt).toLocaleString('vi-VN')}
                  </td>
                  <td style={{ padding: '12px 16px' }}>
                    <div style={{ display: 'flex', gap: 8, alignItems: 'center', flexWrap: 'wrap' }}>
                      <StatusBadge status={row.status} />
                      {row.slaOverdue && <SlaBadge />}
                    </div>
                  </td>
                  <td style={{ padding: '12px 16px', textAlign: 'right', color: '#16a34a', fontWeight: 600, fontSize: 13 }}>
                    Xem chi tiết →
                  </td>
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
