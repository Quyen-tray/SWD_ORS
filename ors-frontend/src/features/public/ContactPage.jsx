export function ContactPage() {
  return (
    <div style={styles.container}>
      <h2>Liên hệ</h2>
      <p>Nếu bạn cần hỗ trợ hoặc muốn tìm hiểu thêm về ORS, hãy liên hệ với chúng tôi.</p>
      <ul style={styles.list}>
        <li>Email: support@ors.example</li>
        <li>Hotline: 1900 000 123</li>
      </ul>
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
  list: {
    paddingLeft: '20px',
    marginTop: '16px',
  },
};
