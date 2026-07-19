import { Link } from 'react-router-dom';
import { AuthLayout } from '../AuthLayout.jsx';

export function RegisterPage() {
  return (
    <AuthLayout
      title="Chào mừng bạn đến với ORS"
      subtitle="Tạo hồ sơ nổi bật và kết nối với nhà tuyển dụng hiệu quả ngay hôm nay."
      imageSrc="/image/register.png"
      footerText="Đã có tài khoản?"
      footerLink="Đăng nhập"
      footerHref="/login"
    >
      <form style={styles.form}>
        <label style={styles.label}>
          Họ và tên
          <input style={styles.input} placeholder="Nhập họ và tên" />
        </label>
        <label style={styles.label}>
          Email
          <input style={styles.input} placeholder="Nhập email" />
        </label>
        <label style={styles.label}>
          Mật khẩu
          <input type="password" style={styles.input} placeholder="Nhập mật khẩu" />
        </label>
        <label style={styles.label}>
          Nhập lại mật khẩu
          <input type="password" style={styles.input} placeholder="Nhập lại mật khẩu" />
        </label>
        <label style={styles.checkboxLabel}>
          <input type="checkbox" style={styles.checkbox} />
          Tôi đã đọc và đồng ý với Điều khoản dịch vụ và Chính sách bảo mật của ORS.
        </label>
        <button style={styles.button}>Đăng ký</button>
      </form>
    </AuthLayout>
  );
}

const styles = {
  form: {
    display: 'grid',
    gap: '16px',
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
  checkboxLabel: {
    display: 'flex',
    alignItems: 'flex-start',
    gap: '12px',
    color: '#475569',
    fontSize: '14px',
    lineHeight: 1.75,
  },
  checkbox: {
    marginTop: '4px',
    width: '18px',
    height: '18px',
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
};
