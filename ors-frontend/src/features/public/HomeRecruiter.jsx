import { Link } from 'react-router-dom';

export function HomeRecruiter() {
  return (
    <div style={styles.wrap}>
      <div style={styles.hero}>
        <div style={styles.left}>
          <h1 style={styles.title}>
            <span>CV Scoring - </span>
            <span style={styles.accent}>Chấm điểm CV</span>
            <div style={styles.subtitleLine}>Chấm điểm và sàng lọc ứng viên hiệu quả</div>
          </h1>

          <div style={styles.ctaRow}>
            <Link to="/contact" style={styles.ctaOutline}>Tư vấn tuyển dụng</Link>
            <Link to="/recruiter/dashboard" style={styles.ctaPrimary}>Trải nghiệm ngay</Link>
          </div>
        </div>

        <div style={styles.right}>
          <div style={styles.heroCard}></div>
          <div style={styles.ghostRobot}></div>
        </div>
      </div>
    </div>
  );
}

const styles = {
  wrap: {
    display: 'flex',
    justifyContent: 'center',
    padding: '20px',
  },
  hero: {
    width: '100%',
    maxWidth: '1280px',
    minHeight: '460px',
    borderRadius: '10px',
    padding: '56px 72px',
    display: 'flex',
    alignItems: 'center',
    gap: '40px',
    background: 'linear-gradient(180deg,#03221b 0%,#043924 65%)',
    boxShadow: '0 20px 70px rgba(3,10,20,0.7)',
    position: 'relative',
    overflow: 'hidden',
  },
  left: {
    flex: 1,
    color: '#e6fff0',
    zIndex: 3,
  },
  title: {
    fontSize: '48px',
    fontWeight: 900,
    lineHeight: 1.02,
    margin: 0,
    color: '#e6fff0',
  },
  accent: {
    color: '#10b981',
    textShadow: '0 10px 40px rgba(16,185,129,0.12)',
    marginLeft: '6px',
  },
  subtitleLine: {
    marginTop: '8px',
    fontSize: '48px',
    fontWeight: 900,
    color: '#e6fff0',
  },
  ctaRow: {
    marginTop: '28px',
    display: 'flex',
    gap: '16px',
  },
  ctaPrimary: {
    padding: '12px 28px',
    borderRadius: '8px',
    background: 'linear-gradient(180deg,#0ee89f,#059e5f)',
    color: '#fff',
    fontWeight: 800,
    textDecoration: 'none',
    boxShadow: '0 12px 40px rgba(16,185,129,0.28), inset 0 -2px 0 rgba(255,255,255,0.06)'
  },
  ctaOutline: {
    padding: '12px 28px',
    borderRadius: '8px',
    color: '#a7f3d0',
    textDecoration: 'none',
    fontWeight: 700,
    border: '2px solid rgba(16,185,129,0.12)',
    background: 'transparent',
  },
  right: {
    width: '520px',
    height: '320px',
    position: 'relative',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  heroCard: {
    width: '420px',
    height: '180px',
    borderRadius: '18px',
    background: 'linear-gradient(90deg, rgba(16,185,129,0.12), rgba(16,185,129,0.04))',
    boxShadow: '0 20px 60px rgba(8,58,42,0.45), 0 6px 20px rgba(16,185,129,0.12)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 4,
  },
  ghostRobot: {
    position: 'absolute',
    right: '-20px',
    bottom: '-40px',
    width: '320px',
    height: '240px',
    borderRadius: '20px',
    background: 'radial-gradient(circle at 25% 25%, #0ef0b0, #068050)',
    transform: 'rotate(-14deg)',
    boxShadow: '0 30px 80px rgba(6,90,58,0.28)',
    zIndex: 2,
  }
};
