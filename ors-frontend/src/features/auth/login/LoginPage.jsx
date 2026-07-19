import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useLogin } from './useLogin.js';
import { AuthLayout } from '../AuthLayout.jsx';

export function LoginPage({
  title,
  subtitle,
  imageSrc,
  footerLink,
  footerHref,
  roleHint,
}) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login, isLoading, error } = useLogin();

  function handleSubmit(e) {
    e.preventDefault();
    login({ email, password });
  }

  return (
    <AuthLayout
      title={title}
      subtitle={subtitle}
      imageSrc={imageSrc}
      footerText="Bạn chưa có tài khoản?"
      footerLink={footerLink}
      footerHref={footerHref}
    >
      <form onSubmit={handleSubmit} style={styles.form}>
        {roleHint && <p style={styles.roleHint}>{roleHint}</p>}
        <label style={styles.label}>
          Email
          <input
            style={styles.input}
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
          />
        </label>
        <label style={styles.label}>
          Mật khẩu
          <input
            type="password"
            style={styles.input}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Mật khẩu"
          />
        </label>
        {error ? <p style={styles.error}>{error}</p> : null}
        <button style={styles.button} disabled={isLoading}>
          {isLoading ? 'Đang đăng nhập...' : 'Đăng nhập'}
        </button>
        <div style={styles.divider}>
          <span style={styles.dividerText}>Hoặc đăng nhập bằng</span>
        </div>
        <div style={styles.socialRow}>
          <button type="button" style={{ ...styles.socialButton, background: '#db4437' }}>Google</button>
          <button type="button" style={{ ...styles.socialButton, background: '#1877f2' }}>Facebook</button>
          <button type="button" style={{ ...styles.socialButton, background: '#0a66c2' }}>LinkedIn</button>
        </div>
        <div style={styles.bottomText}>
          <Link to="/forgot-password" style={styles.link}>Quên mật khẩu?</Link>
        </div>
      </form>
    </AuthLayout>
  );
}

const styles = {
  form: {
    display: 'grid',
    gap: '16px',
  },
  roleHint: {
    margin: 0,
    padding: '10px 14px',
    borderRadius: '14px',
    background: '#ecfdf5',
    color: '#047857',
    fontSize: '14px',
  },
  label: {
    display: 'grid',
    gap: '8px',
    color: '#334155',
    fontWeight: 600,
    fontSize: '14px',
  },
  input: {
    border: '1px solid #d1d5db',
    borderRadius: '14px',
    padding: '14px 16px',
    fontSize: '15px',
    outline: 'none',
    background: '#f8fafc',
  },
  button: {
    width: '100%',
    border: 'none',
    borderRadius: '14px',
    padding: '14px 18px',
    fontSize: '15px',
    fontWeight: 700,
    color: '#fff',
    background: '#10b981',
    cursor: 'pointer',
  },
  error: {
    margin: 0,
    color: '#b91c1c',
    fontSize: '14px',
    fontWeight: 700,
  },
  divider: {
    display: 'flex',
    alignItems: 'center',
    textAlign: 'center',
    color: '#94a3b8',
    fontSize: '13px',
  },
  dividerText: {
    width: '100%',
    borderBottom: '1px solid #e2e8f0',
    lineHeight: '0.1em',
    margin: '0 auto',
    padding: '0 10px',
    background: '#fff',
  },
  socialRow: {
    display: 'grid',
    gap: '12px',
  },
  socialButton: {
    width: '100%',
    border: 'none',
    borderRadius: '14px',
    padding: '12px 16px',
    color: '#fff',
    fontWeight: 700,
    cursor: 'pointer',
  },
  bottomText: {
    display: 'flex',
    justifyContent: 'flex-end',
  },
  link: {
    color: '#10b981',
    textDecoration: 'none',
    fontWeight: 700,
  },
};
