import { useState } from 'react';
import { AuthLayout } from '../AuthLayout.jsx';
import { authApi } from '../authApi.js';

export function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage('');
    setError('');

    if (!email.trim()) {
      setError('Vui lòng nhập email đã đăng ký.');
      return;
    }

    try {
      setIsLoading(true);
      await authApi.forgotPassword(email);
      setMessage('Nếu email đã được đăng ký, liên kết đặt lại mật khẩu sẽ được gửi tới hộp thư của bạn.');
      setEmail('');
    } catch (err) {
      setError(err?.response?.data?.message || 'Không thể gửi liên kết. Vui lòng thử lại.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <AuthLayout
      title="Quên mật khẩu"
      subtitle="Nhập email để nhận liên kết khôi phục mật khẩu mới."
      imageSrc="/image/home.png"
      footerText="Nhớ mật khẩu rồi?"
      footerLink="Đăng nhập"
      footerHref="/login"
    >
      <form style={styles.form} onSubmit={handleSubmit}>
        <label style={styles.label}>
          Email
          <input
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            style={styles.input}
            placeholder="name@example.com"
            required
          />
        </label>
        {error ? <p style={styles.error}>{error}</p> : null}
        {message ? <p style={styles.success}>{message}</p> : null}
        <button style={styles.button} disabled={isLoading}>{isLoading ? 'Đang gửi...' : 'Gửi liên kết'}</button>
      </form>
    </AuthLayout>
  );
}

const styles = {
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '14px',
  },
  label: {
    display: 'flex',
    flexDirection: 'column',
    gap: '6px',
    color: '#334155',
    fontWeight: 600,
    fontSize: '14px',
  },
  input: {
    border: '1px solid #dbe4f0',
    borderRadius: '12px',
    padding: '12px 14px',
    fontSize: '15px',
    outline: 'none',
  },
  button: {
    border: 'none',
    borderRadius: '12px',
    padding: '13px 16px',
    fontSize: '15px',
    fontWeight: 700,
    color: '#fff',
    background: 'linear-gradient(90deg, #2563eb 0%, #1d4ed8 100%)',
    cursor: 'pointer',
  },
  error: {
    margin: 0,
    color: '#b91c1c',
    fontWeight: 700,
    fontSize: '14px',
  },
  success: {
    margin: 0,
    color: '#047857',
    fontWeight: 700,
    fontSize: '14px',
  },
};
