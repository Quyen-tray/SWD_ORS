import { NavLink, Outlet } from 'react-router-dom';
import { MODERATOR_GLOBAL_STYLES, BRAND } from '../features/moderator/shared/ui.jsx';

const NAV_ITEMS = [
  { to: '/moderator/dashboard', icon: '📊', label: 'Dashboard' },
  { to: '/moderator/reports', icon: '🚩', label: 'Report Queue' },
  { to: '/moderator/audit-log', icon: '🗂️', label: 'Audit Log' },
  { to: '/moderator/company-verification', icon: '🏢', label: 'Company Verification' },
  { to: '/moderator/job-moderation', icon: '📋', label: 'Job Moderation' },
];

// Khung UI riêng cho subsystem Moderator. Sidebar/menu của actor này không
// bao giờ xuất hiện trong layout của actor khác (Scope of Control - Ch13).
export function ModeratorLayout() {
  return (
    <div style={{ display: 'flex', minHeight: '100vh', background: '#f8fafc' }}>
      <style>{MODERATOR_GLOBAL_STYLES}</style>
      <aside
        style={{
          width: 250,
          flexShrink: 0,
          background: BRAND.dark,
          color: '#fff',
          padding: 20,
          position: 'sticky',
          top: 0,
          height: '100vh',
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: 4, marginBottom: 4 }}>
          <strong style={{ fontSize: 20, letterSpacing: 0.5 }}>
            OR<span style={{ color: BRAND.accent }}>S</span>
          </strong>
          <span style={{ fontSize: 13, color: '#94a3b8', marginLeft: 6 }}>Moderator</span>
        </div>
        <p style={{ margin: '2px 0 0', fontSize: 12, color: '#64748b' }}>Report Moderation (UC-45..50)</p>

        <nav style={{ marginTop: 28, display: 'flex', flexDirection: 'column', gap: 4 }}>
          {NAV_ITEMS.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) => `mod-navlink${isActive ? ' active' : ''}`}
            >
              <span>{item.icon}</span>
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div style={{ marginTop: 'auto', paddingTop: 16, borderTop: '1px solid rgba(255,255,255,0.08)', fontSize: 12, color: '#64748b' }}>
          Đăng nhập tạm thời: <strong style={{ color: '#cbd5e1' }}>Moderator #4</strong>
        </div>
      </aside>
      <main style={{ flex: 1, padding: '28px 32px', minWidth: 0 }}>
        <div style={{ maxWidth: 1080, margin: '0 auto' }}>
          <Outlet />
        </div>
      </main>
    </div>
  );
}
