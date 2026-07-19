import { Link } from 'react-router-dom';

export function PublicRecruiterHeader() {
  return (
    <header style={styles.header}>
      <div style={styles.leftGroup}>
        <Link to="/" style={styles.brandLink}>
          <img src="/image/topcv-logo.png" alt="logo" style={styles.logo} />
        </Link>
        <nav style={styles.nav}>
          <Link to="/" style={styles.navLink}>Giới thiệu</Link>
          <Link to="/" style={styles.navLink}>Dịch vụ</Link>
          <Link to="/" style={styles.navLink}>Báo giá</Link>
          <Link to="/" style={styles.navLink}>Hỗ trợ</Link>
          <Link to="/blog" style={styles.navLink}>Blog tuyển dụng</Link>
        </nav>
      </div>

      <div style={styles.rightGroup}>
        <div style={styles.langWrap}>
          <div style={styles.langCircle}>VN</div>
        </div>

        <Link to="tel:0900000000" style={styles.consult}>📞 Tư vấn tuyển dụng</Link>

        <Link to="/recruiter-login" style={styles.login}>Đăng nhập</Link>

        <Link to="/recruiter-register" style={styles.postJob}>Đăng tin ngay</Link>
      </div>
    </header>
  );
}

const styles = {
  header: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '14px 28px',
    background: '#ffffff',
    boxShadow: '0 2px 6px rgba(2,6,23,0.06)',
    position: 'sticky',
    top: 0,
    zIndex: 60,
  },
  leftGroup: {
    display: 'flex',
    alignItems: 'center',
    gap: '18px',
  },
  brandLink: {
    display: 'inline-flex',
    alignItems: 'center',
    textDecoration: 'none',
  },
  logo: {
    height: '36px',
  },
  nav: {
    display: 'flex',
    gap: '18px',
    alignItems: 'center',
  },
  navLink: {
    color: '#0f172a',
    textDecoration: 'none',
    fontWeight: 700,
    fontSize: '14px',
  },
  rightGroup: {
    display: 'flex',
    gap: '12px',
    alignItems: 'center',
  },
  langWrap: {
    display: 'flex',
    alignItems: 'center',
  },
  langCircle: {
    width: '36px',
    height: '36px',
    borderRadius: '18px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    background: '#fff',
    border: '1px solid #e6f4ea',
    color: '#10b981',
    fontWeight: 800,
  },
  consult: {
    color: '#065f46',
    textDecoration: 'none',
    fontWeight: 700,
    padding: '8px 12px',
    borderRadius: '8px',
    background: 'linear-gradient(180deg,#f8fff9,#f0fff6)',
  },
  login: {
    padding: '8px 14px',
    borderRadius: '8px',
    color: '#065f46',
    textDecoration: 'none',
    border: '1px solid rgba(6,95,70,0.08)',
    background: 'transparent',
    fontWeight: 700,
  },
  postJob: {
    padding: '10px 18px',
    borderRadius: '8px',
    background: '#10b981',
    color: '#fff',
    textDecoration: 'none',
    fontWeight: 800,
    boxShadow: '0 8px 28px rgba(16,185,129,0.18)'
  }
};
