export function AboutPage() {
  return (
    <div style={styles.container}>
      <h2>Về ORS</h2>
      <p>
        ORS là nền tảng tuyển dụng tích hợp, giúp ứng viên và nhà tuyển dụng kết nối nhanh chóng
        qua công cụ tìm việc, đăng tuyển và quản lý ứng viên chuyên nghiệp.
      </p>
      <p>
        Với giao diện đơn giản và tính năng trực quan, ORS hướng đến trải nghiệm tuyển dụng hiện đại
        và hiệu quả cho mọi quy mô doanh nghiệp.
      </p>
    </div>
  );
}

const styles = {
  container: {
    maxWidth: 780,
    margin: '0 auto',
    padding: '24px',
    background: '#ffffff',
    borderRadius: '24px',
    boxShadow: '0 18px 60px rgba(15, 23, 42, 0.05)',
  },
};
