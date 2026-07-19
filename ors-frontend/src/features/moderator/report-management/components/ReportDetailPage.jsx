import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useReportDetail } from '../hooks/useReportDetail.js';
import { useReportActions } from '../hooks/useReportActions.js';
import { ResolveReportModal } from './ResolveReportModal.jsx';
import { CloseReportModal } from './CloseReportModal.jsx';
import { Card, PageHeader, StatusBadge, SlaBadge, LoadingBlock, ErrorBanner, ActionBadge, AccentButton } from '../../shared/ui.jsx';

const sectionTitleStyle = {
  margin: '0 0 12px',
  fontSize: 13,
  fontWeight: 700,
  color: '#64748b',
  textTransform: 'uppercase',
  letterSpacing: 0.5,
};

// <<boundary>> — UC-46: chi tiết report + hành động Investigate/Resolve/Close. Nút nào
// hiện ra phụ thuộc status hiện tại (chỉ để tránh bấm nhầm) - backend/ReportState vẫn là
// nơi chặn thật nếu FE có hiển thị sai.
export function ReportDetailPage() {
  const { id } = useParams();
  const reportId = Number(id);
  const { report, isLoading, error, refetch } = useReportDetail(reportId);
  const {
    investigate, isInvestigating, investigateError,
    resolve, isResolving, resolveError,
    close, isClosing, closeError,
  } = useReportActions(reportId);
  const [showResolve, setShowResolve] = useState(false);
  const [showClose, setShowClose] = useState(false);

  if (isLoading) return <LoadingBlock />;
  if (error) return <ErrorBanner message={error} />;
  if (!report) return null;

  return (
    <div>
      <Link to="/moderator/reports" style={{ color: '#16a34a', fontSize: 13, fontWeight: 600, textDecoration: 'none' }}>
        ← Quay lại Report Queue
      </Link>

      <PageHeader
        icon="🚩"
        title={`Report #${report.id}`}
        subtitle={new Date(report.createdAt).toLocaleString('vi-VN')}
        actions={
          <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
            <StatusBadge status={report.status} />
            {report.slaOverdue && <SlaBadge />}
          </div>
        }
      />

      <ErrorBanner message={investigateError} />

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginBottom: 16 }}>
        <Card style={{ padding: 20 }}>
          <h4 style={sectionTitleStyle}>Lý do report</h4>
          <p style={{ margin: 0, color: '#0f172a', lineHeight: 1.6 }}>{report.reason}</p>
        </Card>
        <Card style={{ padding: 20 }}>
          <h4 style={sectionTitleStyle}>Tin tuyển dụng bị report</h4>
          {report.entityMissing || !report.reportedEntity ? (
            <p style={{ margin: 0, color: '#94a3b8' }}>Tin tuyển dụng gốc không còn truy cập được.</p>
          ) : (
            <>
              <p style={{ margin: '0 0 4px', fontWeight: 700, color: '#0f172a' }}>{report.reportedEntity.title}</p>
              <p style={{ margin: 0, color: '#475569' }}>{report.reportedEntity.description}</p>
            </>
          )}
        </Card>
      </div>

      <Card style={{ padding: 20, marginBottom: 16 }}>
        <h4 style={sectionTitleStyle}>Lịch sử kiểm duyệt</h4>
        {report.moderationHistory.length === 0 ? (
          <p style={{ margin: 0, color: '#94a3b8' }}>Chưa có hành động nào.</p>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            {report.moderationHistory.map((h) => (
              <div key={h.id} style={{ display: 'flex', gap: 12, alignItems: 'flex-start' }}>
                <span style={{ width: 8, height: 8, borderRadius: '50%', background: '#cbd5e1', marginTop: 6, flexShrink: 0 }} />
                <div>
                  <div style={{ display: 'flex', gap: 8, alignItems: 'center', marginBottom: 2 }}>
                    <ActionBadge action={h.actionType} />
                    <span style={{ fontSize: 12, color: '#94a3b8' }}>{new Date(h.createdAt).toLocaleString('vi-VN')}</span>
                  </div>
                  <p style={{ margin: 0, fontSize: 13, color: '#475569' }}>{h.description}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </Card>

      <div style={{ display: 'flex', gap: 10 }}>
        {report.status === 'PENDING' && (
          <AccentButton onClick={() => investigate(undefined, { onSuccess: refetch })} disabled={isInvestigating}>
            🔍 {isInvestigating ? 'Đang xử lý...' : 'Investigate'}
          </AccentButton>
        )}
        {report.status === 'UNDER_INVESTIGATION' && (
          <AccentButton onClick={() => setShowResolve(true)}>✅ Resolve</AccentButton>
        )}
        {(report.status === 'PENDING' || report.status === 'UNDER_INVESTIGATION') && (
          <AccentButton variant="outline" onClick={() => setShowClose(true)}>✖ Close</AccentButton>
        )}
      </div>

      <ResolveReportModal
        open={showResolve}
        onClose={() => setShowResolve(false)}
        isSubmitting={isResolving}
        error={resolveError}
        onSubmit={(payload) =>
          resolve(payload, {
            onSuccess: () => {
              setShowResolve(false);
              refetch();
            },
          })
        }
      />
      <CloseReportModal
        open={showClose}
        onClose={() => setShowClose(false)}
        isSubmitting={isClosing}
        error={closeError}
        onSubmit={(payload) =>
          close(payload, {
            onSuccess: () => {
              setShowClose(false);
              refetch();
            },
          })
        }
      />
    </div>
  );
}
