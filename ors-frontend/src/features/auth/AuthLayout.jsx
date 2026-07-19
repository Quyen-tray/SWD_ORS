import { Link } from 'react-router-dom';

export function AuthLayout({
  title,
  subtitle,
  imageSrc,
  children,
  footerText,
  footerLink,
  footerHref,
}) {
  return (
    <div style={styles.page}>
      <div style={styles.card}>
        <div style={styles.illustrationPanel}>
          <div style={styles.heroContent}>
            <div style={styles.logo}>ORS</div>
            <div style={styles.heroTitle}>Tiếp lợi thế, nối thành công</div>
            <p style={styles.heroSubtitle}>ORS - Nền tảng tuyển dụng và tìm việc ứng dụng công nghệ tại Việt Nam</p>
            <img src={imageSrc} alt="Auth illustration" style={styles.image} />
          </div>
        </div>
        <div style={styles.formPanel}>
          <div style={styles.brand}>ORS</div>
          <h1 style={styles.title}>{title}</h1>
          <p style={styles.subtitle}>{subtitle}</p>
          {children}
          {footerText && footerHref && (
            <p style={styles.footerText}>
              {footerText}{' '}
              <Link to={footerHref} style={styles.link}>
                {footerLink}
              </Link>
            </p>
          )}
        </div>
      </div>
    </div>
  );
}

const styles = {
  page: {
    minHeight: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '24px',
    background: 'linear-gradient(180deg, #0f172a 0%, #031317 100%)',
  },
  card: {
    width: '100%',
    maxWidth: '1080px',
    minHeight: '720px',
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    borderRadius: '28px',
    overflow: 'hidden',
    boxShadow: '0 24px 80px rgba(0, 0, 0, 0.28)',
    background: '#0b1722',
  },
  illustrationPanel: {
    position: 'relative',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    background: 'linear-gradient(180deg, rgba(14, 47, 72, 0.96), rgba(4, 111, 75, 0.96))',
    padding: '32px',
  },
  heroContent: {
    width: '100%',
    maxWidth: '420px',
    display: 'grid',
    gap: '24px',
    color: '#f8fafc',
  },
  logo: {
    color: '#10b981',
    fontSize: '1.25rem',
    fontWeight: 800,
  },
  heroTitle: {
    margin: 0,
    fontSize: '2.5rem',
    fontWeight: 800,
    lineHeight: 1.05,
    color: '#f8fafc',
  },
  heroSubtitle: {
    margin: 0,
    color: 'rgba(248,250,252,0.78)',
    lineHeight: 1.8,
  },
  image: {
    width: '100%',
    borderRadius: '24px',
    objectFit: 'cover',
  },
  formPanel: {
    padding: '48px 44px',
    background: '#fff',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
  },
  brand: {
    width: 'fit-content',
    padding: '8px 14px',
    borderRadius: '999px',
    background: '#ecfdf5',
    color: '#047857',
    fontWeight: 700,
    fontSize: '14px',
    marginBottom: '24px',
  },
  title: {
    margin: '0 0 12px',
    fontSize: '32px',
    color: '#0f172a',
  },
  subtitle: {
    margin: '0 0 28px',
    color: '#475569',
    lineHeight: 1.7,
    maxWidth: '520px',
  },
  footerText: {
    marginTop: '22px',
    color: '#64748b',
    fontSize: '14px',
  },
  link: {
    color: '#10b981',
    fontWeight: 700,
    textDecoration: 'none',
  },
};
