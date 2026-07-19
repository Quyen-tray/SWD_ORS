import { AuthLayout } from '../AuthLayout.jsx';

export function ChangePasswordPage() {
  return (
    <AuthLayout
      title="Đổi mật khẩu"
      subtitle="Bảo mật tài khoản của bạn bằng cách thay đổi mật khẩu thường xuyên."
      imageSrc="/image/login_nha_tuyen_dung.png"
      footerText="Quay lại"
      footerLink="Trang chủ"
      footerHref="/"
    >
      <form style={styles.form}>
        <label style={styles.label}>
          Mật khẩu hiện tại
          <input type="password" style={styles.input} placeholder="••••••••" />
        </label>
        <label style={styles.label}>
          Mật khẩu mới
          <input type="password" style={styles.input} placeholder="••••••••" />
        </label>
        <label style={styles.label}>
          Xác nhận mật khẩu mới
          <input type="password" style={styles.input} placeholder="••••••••" />
        </label>
        <button style={styles.button}>Cập nhật mật khẩu</button>
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
};
