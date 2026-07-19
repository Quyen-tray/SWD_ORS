import { Outlet } from 'react-router-dom';
import { PublicHeader } from '../shared/components/PublicHeader.jsx';

export function PublicLayout() {
  return (
    <div style={styles.page}>
      <PublicHeader />
      <main style={styles.main}>
        <Outlet />
      </main>
      <footer style={styles.footer}>© 2026 ORS. All rights reserved.</footer>
    </div>
  );
}

const styles = {
  page: {
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    background: '#061d14',
    color: '#f8fafc',
  },
  header: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: '16px',
    padding: '20px 32px',
    background: '#052617',
    borderBottom: '1px solid rgba(255,255,255,0.08)',
  },
  brand: {
    fontSize: '1.5rem',
    fontWeight: 800,
    color: '#d1fae5',
    letterSpacing: '0.08em',
  },
  nav: {
    display: 'flex',
    alignItems: 'center',
    gap: '22px',
    flexWrap: 'wrap',
  },
  link: {
    color: '#d1fae5',
    textDecoration: 'none',
    fontWeight: 600,
  },
  actions: {
    display: 'flex',
    gap: '12px',
  },
  buttonPrimary: {
    padding: '10px 22px',
    borderRadius: '999px',
    background: '#10b981',
    color: '#fff',
    textDecoration: 'none',
    fontWeight: 700,
  },
  buttonSecondary: {
    padding: '10px 22px',
    borderRadius: '999px',
    background: 'rgba(255,255,255,0.08)',
    color: '#f8fafc',
    textDecoration: 'none',
    fontWeight: 700,
    border: '1px solid rgba(255,255,255,0.14)',
  },
  buttonTertiary: {
    padding: '10px 22px',
    borderRadius: '999px',
    background: 'rgba(16,185,129,0.12)',
    color: '#a7f3d0',
    textDecoration: 'none',
    fontWeight: 700,
    border: '1px solid rgba(16,185,129,0.22)',
  },
  main: {
    flex: 1,
    padding: '24px 32px 36px',
  },
  footer: {
    padding: '18px 32px',
    textAlign: 'center',
    borderTop: '1px solid rgba(255,255,255,0.08)',
    background: '#02140c',
    color: '#94a3b8',
  },
};
