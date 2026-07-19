// <<boundary>> — bộ UI dùng CHUNG RIÊNG cho subsystem Moderator (dashboard/report-management/
// audit-log/layout). KHÔNG đặt vào src/shared/ (dùng chung toàn app, ảnh hưởng mọi actor) -
// đây chỉ là style/khối trình bày lặp lại trong phạm vi Moderator, giữ 3 feature nhất quán
// nhau về màu sắc/khoảng cách mà không phải sửa Button/Modal/Table/Pagination dùng chung.

// Bảng màu theo brand ORS (xanh lá đậm cho nền tối + xanh lá tươi cho accent/nút chính) -
// chỉ dùng trong phạm vi Moderator, KHÔNG đổi màu Button/Modal dùng chung (những actor khác
// vẫn giữ nguyên giao diện hiện có của họ).
export const BRAND = {
  dark: '#0b2e23',
  darkSoft: '#123a2c',
  accent: '#16a34a',
  accentHover: '#15803d',
  accentSoft: '#dcfce7',
};

export const STATUS_META = {
  PENDING: { label: 'Pending', bg: '#fef3c7', color: '#92400e', dot: '#f59e0b' },
  UNDER_INVESTIGATION: { label: 'Under Investigation', bg: '#dbeafe', color: '#1e40af', dot: '#2563eb' },
  RESOLVED: { label: 'Resolved', bg: '#dcfce7', color: '#166534', dot: '#16a34a' },
  CLOSED: { label: 'Closed', bg: '#f1f5f9', color: '#475569', dot: '#64748b' },
};

const ACTION_META = {
  REMOVE_POSTING: { bg: '#fee2e2', color: '#991b1b' },
  SUSPEND_COMPANY: { bg: '#fee2e2', color: '#991b1b' },
  ISSUE_WARNING: { bg: '#fef9c3', color: '#854d0e' },
  INVESTIGATION_STARTED: { bg: '#dbeafe', color: '#1e40af' },
  REPORT_RESOLVED: { bg: '#dcfce7', color: '#166534' },
  REPORT_CLOSED: { bg: '#f1f5f9', color: '#475569' },
  REPORT_QUEUE_ACCESS: { bg: '#f5f3ff', color: '#5b21b6' },
  DASHBOARD_ACCESS: { bg: '#f5f3ff', color: '#5b21b6' },
  AUDIT_LOG_ACCESS: { bg: '#f5f3ff', color: '#5b21b6' },
};

export function StatusBadge({ status }) {
  const meta = STATUS_META[status] ?? { label: status, bg: '#f1f5f9', color: '#475569', dot: '#94a3b8' };
  return (
    <span
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 6,
        padding: '3px 10px',
        borderRadius: 999,
        background: meta.bg,
        color: meta.color,
        fontSize: 12,
        fontWeight: 700,
        letterSpacing: 0.2,
        whiteSpace: 'nowrap',
      }}
    >
      <span style={{ width: 6, height: 6, borderRadius: '50%', background: meta.dot }} />
      {meta.label}
    </span>
  );
}

export function ActionBadge({ action }) {
  const meta = ACTION_META[action] ?? { bg: '#f1f5f9', color: '#475569' };
  return (
    <span
      style={{
        display: 'inline-block',
        padding: '2px 8px',
        borderRadius: 6,
        background: meta.bg,
        color: meta.color,
        fontSize: 12,
        fontWeight: 600,
        whiteSpace: 'nowrap',
      }}
    >
      {action}
    </span>
  );
}

export function SlaBadge() {
  return (
    <span
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 4,
        padding: '2px 8px',
        borderRadius: 999,
        background: '#fee2e2',
        color: '#b91c1c',
        fontSize: 11,
        fontWeight: 700,
      }}
    >
      ⚠ Quá hạn SLA
    </span>
  );
}

// Nút pill bo tròn theo style ORS (solid xanh lá / outline xanh lá / solid đỏ cho hành động
// đóng-report). Component riêng của Moderator - src/shared/components/Button.jsx (dùng
// chung mọi actor) vẫn giữ nguyên style hiện có, không bị đổi theo.
const ACCENT_VARIANTS = {
  solid: { background: BRAND.accent, color: '#fff', border: 'none' },
  outline: { background: '#fff', color: BRAND.accent, border: `1.5px solid ${BRAND.accent}` },
  danger: { background: '#dc2626', color: '#fff', border: 'none' },
  plain: { background: 'transparent', color: '#64748b', border: 'none' },
};

export function AccentButton({ children, variant = 'solid', disabled, style, ...props }) {
  return (
    <button
      disabled={disabled}
      style={{
        padding: '10px 22px',
        borderRadius: 999,
        fontWeight: 700,
        fontSize: 14,
        cursor: disabled ? 'not-allowed' : 'pointer',
        opacity: disabled ? 0.6 : 1,
        transition: 'filter 0.15s, transform 0.15s',
        ...ACCENT_VARIANTS[variant],
        ...style,
      }}
      onMouseEnter={(e) => !disabled && (e.currentTarget.style.filter = 'brightness(0.94)')}
      onMouseLeave={(e) => (e.currentTarget.style.filter = 'none')}
      {...props}
    >
      {children}
    </button>
  );
}

export function Card({ children, style, className = '', ...props }) {
  return (
    <div
      className={`mod-card ${className}`}
      style={{
        background: '#fff',
        border: '1px solid #e5e7eb',
        borderRadius: 12,
        boxShadow: '0 1px 2px rgba(15, 23, 42, 0.04)',
        ...style,
      }}
      {...props}
    >
      {children}
    </div>
  );
}

export function PageHeader({ icon, title, subtitle, actions }) {
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'flex-start',
        marginBottom: 24,
        flexWrap: 'wrap',
        gap: 12,
      }}
    >
      <div>
        <h2 style={{ margin: 0, fontSize: 24, fontWeight: 800, color: '#0f172a', display: 'flex', alignItems: 'center', gap: 10 }}>
          {icon && <span style={{ fontSize: 22 }}>{icon}</span>}
          {title}
        </h2>
        {subtitle && <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: 14 }}>{subtitle}</p>}
      </div>
      {actions && <div style={{ display: 'flex', gap: 8 }}>{actions}</div>}
    </div>
  );
}

export function EmptyState({ icon = '📭', message = 'Không có dữ liệu.' }) {
  return (
    <div style={{ textAlign: 'center', padding: '48px 16px', color: '#94a3b8' }}>
      <div style={{ fontSize: 32, marginBottom: 8 }}>{icon}</div>
      <p style={{ margin: 0 }}>{message}</p>
    </div>
  );
}

export function LoadingBlock({ message = 'Đang tải dữ liệu...' }) {
  return (
    <div style={{ textAlign: 'center', padding: '48px 16px', color: '#94a3b8' }}>
      <div className="mod-spinner" />
      <p style={{ margin: '12px 0 0' }}>{message}</p>
    </div>
  );
}

export function ErrorBanner({ message }) {
  if (!message) return null;
  return (
    <div
      style={{
        background: '#fef2f2',
        border: '1px solid #fecaca',
        color: '#b91c1c',
        borderRadius: 8,
        padding: '10px 14px',
        fontSize: 14,
        marginBottom: 16,
      }}
    >
      ⚠ {message}
    </div>
  );
}

// Ghép 1 lần trong ModeratorLayout - class prefix "mod-" tránh đụng style của actor khác.
export const MODERATOR_GLOBAL_STYLES = `
  .mod-card { transition: box-shadow 0.15s ease, transform 0.15s ease; }
  .mod-card--hoverable:hover { box-shadow: 0 4px 14px rgba(15, 23, 42, 0.08); transform: translateY(-1px); }
  .mod-navlink { display: flex; align-items: center; gap: 10px; padding: 10px 12px; border-radius: 8px;
    color: #cbd5e1; text-decoration: none; font-size: 14px; font-weight: 500; transition: background 0.15s, color 0.15s; }
  .mod-navlink:hover { background: rgba(255,255,255,0.06); color: #fff; }
  .mod-navlink.active { background: ${BRAND.accent}; color: #fff; font-weight: 700; }
  .mod-table-row:hover { background: #f8fafc; }
  .mod-spinner { display: inline-block; width: 28px; height: 28px; border: 3px solid #e2e8f0;
    border-top-color: ${BRAND.accent}; border-radius: 50%; animation: mod-spin 0.8s linear infinite; }
  @keyframes mod-spin { to { transform: rotate(360deg); } }
`;
