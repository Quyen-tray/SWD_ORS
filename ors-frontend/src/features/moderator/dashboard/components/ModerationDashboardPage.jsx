import { useNavigate } from 'react-router-dom';
import { useModerationDashboard } from '../hooks/useModerationDashboard.js';
import { Card, PageHeader, LoadingBlock, ErrorBanner, AccentButton } from '../../shared/ui.jsx';

// <<boundary>> — UC-49 (Facade, ModerationDashboardService gộp 3 repository ở backend).
// Field null nghĩa là panel đó lỗi (EF-01) - hiển thị thông báo riêng, không làm hỏng
// các panel còn lại.
const TILES = [
  { key: 'openReportsCount', icon: '🚩', title: 'Open Reports', color: '#2563eb', suffix: '' },
  { key: 'slaCompliancePercentToday', icon: '⏱️', title: 'SLA Compliance', color: '#16a34a', suffix: '%', decimals: 1 },
  { key: 'pendingCompanyVerificationCount', icon: '🏢', title: 'Pending Company Verification', color: '#d97706', suffix: '' },
  { key: 'pendingJobPostingReviewCount', icon: '📋', title: 'Pending Job Posting Review', color: '#7c3aed', suffix: '' },
];

function StatTile({ icon, title, value, suffix, color }) {
  const failed = value === null || value === undefined;
  return (
    <Card className="mod-card--hoverable" style={{ padding: 20, flex: '1 1 200px', minWidth: 200 }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 10 }}>
        <span
          style={{
            width: 36, height: 36, borderRadius: 10, background: `${color}1a`,
            display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18,
          }}
        >
          {icon}
        </span>
        <span style={{ color: '#64748b', fontSize: 13, fontWeight: 600 }}>{title}</span>
      </div>
      {failed ? (
        <div style={{ color: '#dc2626', fontSize: 13 }}>Không tải được số liệu này.</div>
      ) : (
        <div style={{ fontSize: 30, fontWeight: 800, color: '#0f172a' }}>
          {value}
          <span style={{ fontSize: 18, color }}>{suffix}</span>
        </div>
      )}
    </Card>
  );
}

export function ModerationDashboardPage() {
  const { summary, isLoading, error } = useModerationDashboard();
  const navigate = useNavigate();

  return (
    <div>
      <PageHeader icon="📊" title="Moderation Dashboard" subtitle="Tổng quan hàng đợi report và các khu vực đang chờ xử lý." />
      <ErrorBanner message={error} />
      {isLoading ? (
        <LoadingBlock />
      ) : (
        <>
          <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
            {TILES.map((tile) => {
              const raw = summary?.[tile.key];
              const value = typeof raw === 'number' && tile.decimals ? raw.toFixed(tile.decimals) : raw;
              return <StatTile key={tile.key} icon={tile.icon} title={tile.title} value={value} suffix={tile.suffix} color={tile.color} />;
            })}
          </div>

          <div style={{ display: 'flex', gap: 12, marginTop: 28 }}>
            <AccentButton variant="solid" onClick={() => navigate('/moderator/reports')}>
              🚩 Xem Report Queue
            </AccentButton>
            <AccentButton variant="outline" onClick={() => navigate('/moderator/audit-log')}>
              🗂️ Xem Audit Log
            </AccentButton>
          </div>
        </>
      )}
    </div>
  );
}
