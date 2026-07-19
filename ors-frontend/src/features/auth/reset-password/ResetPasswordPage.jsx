import { useMemo, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { AuthLayout } from '../AuthLayout.jsx';
import { authApi } from '../authApi.js';

export function ResetPasswordPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const token = useMemo(() => new URLSearchParams(location.search).get('token') || '', [location.search]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage('');
    setError('');

    if (!token) {
      setError('Liên kết đặt lại mật khẩu không hợp lệ.');
      return;
    }

    if (password.length < 8) {
      setError('Mật khẩu phải có ít nhất 8 ký tự.');
      return;
    }

    if (password !== confirmPassword) {
      setError('Mật khẩu xác nhận không khớp.');
      return;
    }

    try {
      setIsLoading(true);
      await authApi.resetPassword({ token, password });
      setMessage('Mật khẩu đã được cập nhật. Bạn có thể đăng nhập lại ngay.');
      setPassword('');
      setConfirmPassword('');
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      setError(err?.response?.data?.message || 'Không thể đặt lại mật khẩu.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <AuthLayout
      title="Đặt lại mật khẩu"
      subtitle="Tạo mật khẩu mới để tiếp tục sử dụng tài khoản của bạn."
      imageSrc="/image/home.png"
      footerText="Quay lại"
      footerLink="Đăng nhập"
      footerHref="/login"
    >
      <form style={styles.form} onSubmit={handleSubmit}>
        <label style={styles.label}>
          Mật khẩu mới
          <input type="password" value={password} onChange={(event) => setPassword(event.target.value)} style={styles.input} placeholder="••••••••" required />
        </label>
        <label style={styles.label}>
          Xác nhận mật khẩu
          <input type="password" value={confirmPassword} onChange={(event) => setConfirmPassword(event.target.value)} style={styles.input} placeholder="••••••••" required />
        </label>
        {error ? <p style={styles.error}>{error}</p> : null}
        {message ? <p style={styles.success}>{message}</p> : null}
        <button style={styles.button} disabled={isLoading}>{isLoading ? 'Đang xử lý...' : 'Đặt lại mật khẩu'}</button>
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
