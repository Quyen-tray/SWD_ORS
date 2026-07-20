import { NavLink, Outlet } from 'react-router-dom';

// Khung UI riêng cho subsystem Recruiter. Sidebar/menu của actor này không
// bao giờ xuất hiện trong layout của actor khác (Scope of Control - Ch13).
//
// Menu chỉ liệt kê 2 trang thuộc 5 UC đã hoàn thiện (UC-01, UC-04..07 - xem
// 00_KE_HOACH_TONG_QUAN.md). communication/reporting/jobs vẫn còn scaffold rỗng
// (ngoài phạm vi đồ án) nên chưa thêm vào đây - thêm sau nếu module đó có người làm.
const NAV_ITEMS = [
  { to: '/recruiter/candidates', label: 'Danh sách ứng viên' },
  { to: '/recruiter/recruitment-workflow', label: 'Pipeline tuyển dụng' },
];

export function RecruiterLayout() {
  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <aside style={{ width: 240, background: '#111827', color: '#fff', padding: 16 }}>
        <strong>ORS — Recruiter</strong>
        <nav style={{ display: 'flex', flexDirection: 'column', gap: 4, marginTop: 20 }}>
          {NAV_ITEMS.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              style={({ isActive }) => ({
                color: isActive ? '#10b981' : '#e5e7eb',
                background: isActive ? 'rgba(16,185,129,0.12)' : 'transparent',
                textDecoration: 'none',
                padding: '10px 12px',
                borderRadius: 8,
                fontSize: 14,
                fontWeight: isActive ? 700 : 500,
              })}
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>
      <main style={{ flex: 1, padding: 24 }}>
        <Outlet />
      </main>
    </div>
  );
}
