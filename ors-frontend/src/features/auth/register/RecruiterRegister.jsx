import { useState } from 'react';
import { AuthLayout } from '../AuthLayout.jsx';
import { authApi } from '../authApi.js';

const initialForm = {
  email: '',
  password: '',
  confirmPassword: '',
  fullName: '',
  gender: '',
  phoneNumber: '',
  companyName: '',
  workLocation: '',
  acceptTerms: false,
  marketingConsent: false,
};

export function RecruiterRegister() {
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (event) => {
    const { name, value, type, checked } = event.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    setMessage('');

    if (!form.acceptTerms) {
      setError('Bạn phải đồng ý với Điều khoản dịch vụ và Chính sách Quyền riêng tư để tiếp tục.');
      return;
    }

    if (form.password !== form.confirmPassword) {
      setError('Mật khẩu và nhập lại mật khẩu không khớp.');
      return;
    }

    try {
      setSubmitting(true);
      await authApi.registerRecruiter({
        email: form.email,
        password: form.password,
        confirmPassword: form.confirmPassword,
        fullName: form.fullName,
        gender: form.gender,
        phoneNumber: form.phoneNumber,
        companyName: form.companyName,
        workLocation: form.workLocation,
        acceptTerms: form.acceptTerms,
      });
      setMessage('Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.');
      setForm(initialForm);
    } catch (err) {
      setError(err?.response?.data?.message || 'Đăng ký thất bại. Vui lòng thử lại.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AuthLayout
      title="Đăng ký tài khoản Nhà tuyển dụng"
      subtitle="Cùng tạo dựng lợi thế cho doanh nghiệp bằng trải nghiệm công nghệ tuyển dụng ứng dụng AI & Hiring Funnel"
      imageSrc="/image/register_recruiter.png"
      footerText="Đã có tài khoản?"
      footerLink="Đăng nhập ngay"
      footerHref="/recruiter-login"
    >
      <form style={styles.form} onSubmit={handleSubmit}>
        <section style={styles.box}>
          <h3 style={styles.sectionTitle}>Quy định</h3>
          <p style={styles.note}>Để đảm bảo chất lượng dịch vụ, ORS không cho phép một người dùng tạo nhiều tài khoản khác nhau. Vui lòng dùng email doanh nghiệp khi đăng ký tài khoản nhà tuyển dụng.</p>
        </section>

        <section style={styles.box}>
          <h3 style={styles.sectionTitle}>Tài khoản</h3>
          <label style={styles.label}>
            Email đăng nhập *
            <input name="email" type="email" required style={styles.input} placeholder="Email" value={form.email} onChange={handleChange} />
          </label>
          <label style={styles.label}>
            Mật khẩu *
            <input name="password" type="password" required style={styles.input} placeholder="Mật khẩu" value={form.password} onChange={handleChange} />
          </label>
          <label style={styles.label}>
            Nhập lại mật khẩu *
            <input name="confirmPassword" type="password" required style={styles.input} placeholder="Nhập lại mật khẩu" value={form.confirmPassword} onChange={handleChange} />
          </label>
        </section>

        <section style={styles.box}>
          <h3 style={styles.sectionTitle}>Thông tin nhà tuyển dụng</h3>
          <label style={styles.label}>
            Họ và tên *
            <input name="fullName" required style={styles.input} placeholder="Họ và tên" value={form.fullName} onChange={handleChange} />
          </label>
          <div style={styles.row}>
            <label style={{...styles.label, flex: 1}}>
              Số điện thoại cá nhân
              <input name="phoneNumber" style={styles.input} placeholder="Số điện thoại" value={form.phoneNumber} onChange={handleChange} />
            </label>
            <label style={{...styles.label, flex: 1}}>
              Công ty *
              <input name="companyName" required style={styles.input} placeholder="Tên công ty" value={form.companyName} onChange={handleChange} />
            </label>
          </div>

          <div style={styles.row}>
            <label style={{...styles.label, flex: 1}}>
              Địa điểm làm việc *
              <select name="workLocation" required style={styles.input} value={form.workLocation} onChange={handleChange}>
                <option value="">Chọn tỉnh/thành phố</option>
                <option value="Hà Nội">Hà Nội</option>
                <option value="TP. Hồ Chí Minh">TP. Hồ Chí Minh</option>
                <option value="Đà Nẵng">Đà Nẵng</option>
                <option value="Hải Phòng">Hải Phòng</option>
                <option value="Cần Thơ">Cần Thơ</option>
                <option value="Bình Dương">Bình Dương</option>
                <option value="Đắk Nông">Đắk Nông</option>
                <option value="Quảng Ninh">Quảng Ninh</option>
              </select>
            </label>
            <label style={{...styles.label, flex: 1}}>
              Giới tính
              <select name="gender" style={styles.input} value={form.gender} onChange={handleChange}>
                <option value="">Chọn giới tính</option>
                <option value="MALE">Nam</option>
                <option value="FEMALE">Nữ</option>
                <option value="OTHER">Khác</option>
              </select>
            </label>
          </div>
        </section>

        {error ? <p style={styles.error}>{error}</p> : null}
        {message ? <p style={styles.success}>{message}</p> : null}

        <label style={styles.checkboxLabel}>
          <input name="acceptTerms" type="checkbox" checked={form.acceptTerms} onChange={handleChange} style={styles.checkbox} />
          Tôi đã đọc và đồng ý với Điều khoản dịch vụ và Chính sách Quyền riêng tư của ORS.
        </label>

        <label style={styles.checkboxLabel}>
          <input name="marketingConsent" type="checkbox" checked={form.marketingConsent} onChange={handleChange} style={styles.checkbox} />
          Tôi đồng ý nhận thông tin tư vấn và hỗ trợ từ ORS.
        </label>

        <button style={styles.button} type="submit" disabled={submitting}>{submitting ? 'Đang gửi...' : 'Hoàn tất'}</button>
      </form>
    </AuthLayout>
  );
}

const styles = {
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '18px',
  },
  box: {
    padding: '18px',
    borderRadius: '8px',
    background: '#ffffff',
    border: '1px solid rgba(16,185,129,0.06)'
  },
  sectionTitle: {
    margin: 0,
    color: '#065f46',
    fontWeight: 800,
    marginBottom: '12px',
  },
  note: {
    color: '#374151',
    fontSize: '14px',
  },
  label: {
    display: 'block',
    marginBottom: '8px',
    color: '#0f172a',
    fontWeight: 700,
    fontSize: '14px',
  },
  input: {
    width: '100%',
    padding: '12px 14px',
    borderRadius: '10px',
    border: '1px solid #e6eef0',
    marginTop: '6px',
    fontSize: '14px',
  },
  row: {
    display: 'flex',
    gap: '12px',
    marginTop: '8px',
  },
  checkboxLabel: {
    display: 'flex',
    gap: '12px',
    alignItems: 'flex-start',
    color: '#334155',
    fontSize: '14px',
  },
  checkbox: {
    width: '18px',
    height: '18px',
    marginTop: '4px',
  },
  button: {
    marginTop: '6px',
    padding: '12px 18px',
    background: '#10b981',
    color: '#fff',
    fontWeight: 800,
    border: 'none',
    borderRadius: '10px',
    cursor: 'pointer',
  },
  error: {
    color: '#b91c1c',
    fontWeight: 700,
    fontSize: '14px',
    margin: 0,
  },
  success: {
    color: '#065f46',
    fontWeight: 700,
    fontSize: '14px',
    margin: 0,
  }
};
