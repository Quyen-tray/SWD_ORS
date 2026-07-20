import { NavLink, Outlet } from 'react-router-dom';

// Khung UI riêng cho subsystem Admin. Sidebar/menu của actor này không
// bao giờ xuất hiện trong layout của actor khác (Scope of Control - Ch13).
export function AdminLayout() {
  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <aside style={styles.sidebar}>
        <div style={styles.brand}>ORS — Admin</div>
        <nav style={styles.nav}>
          <NavLink to="/admin/dashboard" style={navLinkStyle}>Tổng quan</NavLink>
          <NavLink to="/admin/users" style={navLinkStyle}>Người dùng</NavLink>
          <NavLink to="/admin/job-categories" style={navLinkStyle}>Danh mục ngành nghề</NavLink>
          <NavLink to="/admin/audit-logs" style={navLinkStyle}>Nhật ký kiểm toán</NavLink>
        </nav>
      </aside>
      <main style={{ flex: 1, padding: 24 }}>
        <Outlet />
      </main>
    </div>
  );
}

// NavLink truyền isActive vào hàm style để tô đậm mục đang mở.
function navLinkStyle({ isActive }) {
  return {
    display: 'block',
    padding: '10px 12px',
    borderRadius: 6,
    marginBottom: 4,
    textDecoration: 'none',
    color: isActive ? '#fff' : '#cbd5e1',
    background: isActive ? '#2563eb' : 'transparent',
    fontWeight: isActive ? 600 : 400,
  };
}

const styles = {
  sidebar: { width: 240, background: '#111827', color: '#fff', padding: 16 },
  brand: { fontWeight: 700, marginBottom: 20 },
  nav: { display: 'flex', flexDirection: 'column' },
};
