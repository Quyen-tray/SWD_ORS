import { Outlet } from 'react-router-dom';

// Khung UI riêng cho subsystem Candidate. Sidebar/menu của actor này không
// bao giờ xuất hiện trong layout của actor khác (Scope of Control - Ch13).
export function CandidateLayout() {
  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <aside style={{ width: 240, background: '#111827', color: '#fff', padding: 16 }}>
        <strong>ORS — Candidate</strong>
        {/* TODO: menu điều hướng riêng cho Candidate */}
      </aside>
      <main style={{ flex: 1, padding: 24 }}>
        <Outlet />
      </main>
    </div>
  );
}
