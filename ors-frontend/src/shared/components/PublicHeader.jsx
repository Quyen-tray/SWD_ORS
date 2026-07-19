import { Link } from 'react-router-dom';

export function PublicHeader() {
  return (
    <header style={styles.header}>
      <div style={styles.brand}>ORS</div>
      <nav style={styles.nav}>
        <Link to="/" style={styles.link}>Việc làm</Link>
        <Link to="/" style={styles.link}>Tạo CV</Link>
        <Link to="/" style={styles.link}>Công cụ</Link>
        <Link to="/" style={styles.link}>Cẩm nang</Link>
        <Link to="/" style={styles.link}>TopCV Pro</Link>
      </nav>
      <div style={styles.actions}>
        <Link to="/public_recruiter" style={styles.buttonTertiary}>Đăng tuyển & tìm hồ sơ</Link>
        <Link to="/register" style={styles.buttonSecondary}>Đăng ký</Link>
        <Link to="/login" style={styles.buttonPrimary}>Đăng nhập</Link>
      </div>
    </header>
  );
}

const styles = {
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
};
