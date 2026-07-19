import { Link } from 'react-router-dom';

const categories = [
  'Kinh doanh/Bán hàng',
  'Marketing/PR/Quảng cáo',
  'Chăm sóc khách hàng',
  'Nhân sự/Hành chính/Pháp chế',
  'Công nghệ thông tin',
  'Lao động phổ thông',
];

export function HomePage() {
  return (
    <div style={styles.page}>
      <div style={styles.heroSection}>
        <div style={styles.searchBlock}>
          <div style={styles.heroTitle}>TopCV - Tạo CV, Tìm việc làm, Tuyển dụng hiệu quả</div>
          <div style={styles.searchCard}>
            <div style={styles.searchInputGroup}>
              <input placeholder="Vị trí tuyển dụng, tên công ty" style={styles.searchInput} />
              <button style={styles.searchButton}>Tìm kiếm</button>
            </div>
            <div style={styles.location}>Địa điểm</div>
          </div>
          <div style={styles.statsCard}>
            <div>Thị trường việc làm hôm nay</div>
            <div style={styles.statsRow}>
              <span>Việc làm đang tuyển</span>
              <strong>56.359</strong>
            </div>
            <div style={styles.statsRow}>
              <span>Việc làm mới hôm nay</span>
              <strong>731</strong>
            </div>
          </div>
        </div>
        <div style={styles.rightPanel}>
          <div style={styles.heroCard}>
            <div style={styles.heroCardTitle}>Tiếp lợi thế, nối thành công</div>
            <div style={styles.heroCardText}>ORS - Hệ sinh thái nhân sự tiên phong ứng dụng công nghệ tại Việt Nam</div>
          </div>
        </div>
      </div>

      <div style={styles.categorySection}>
        <div style={styles.categoryHeader}>Danh mục ngành nghề</div>
        <div style={styles.categoryGrid}>
          {categories.map((item) => (
            <div key={item} style={styles.categoryItem}>
              <span>{item}</span>
              <span style={styles.arrow}>&gt;</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

const styles = {
  page: {
    minHeight: '100vh',
    padding: '24px',
    background: 'linear-gradient(180deg, #042f21 0%, #0e412f 55%, #071d24 100%)',
    color: '#f8fafc',
    display: 'grid',
    gap: '24px',
  },
  topBar: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    gap: '16px',
  },
  brand: {
    fontSize: '1.5rem',
    fontWeight: 700,
    letterSpacing: '0.1em',
  },
  topLinks: {
    display: 'flex',
    gap: '18px',
    flexWrap: 'wrap',
  },
  link: {
    color: '#d1fae5',
    textDecoration: 'none',
    fontWeight: 500,
  },
  topActions: {
    display: 'flex',
    gap: '12px',
  },
  buttonPrimary: {
    padding: '10px 20px',
    borderRadius: '999px',
    background: '#10b981',
    color: '#fff',
    textDecoration: 'none',
    fontWeight: 700,
  },
  buttonSecondary: {
    padding: '10px 20px',
    borderRadius: '999px',
    background: 'rgba(255,255,255,0.08)',
    color: '#f8fafc',
    textDecoration: 'none',
    fontWeight: 700,
    border: '1px solid rgba(255,255,255,0.16)',
  },
  heroSection: {
    display: 'grid',
    gridTemplateColumns: '1.3fr 1fr',
    gap: '24px',
    alignItems: 'stretch',
  },
  searchBlock: {
    display: 'grid',
    gap: '20px',
    background: 'rgba(255,255,255,0.06)',
    border: '1px solid rgba(255,255,255,0.12)',
    borderRadius: '32px',
    padding: '30px',
    minHeight: '420px',
  },
  heroTitle: {
    fontSize: '2.5rem',
    fontWeight: 800,
    lineHeight: 1.05,
    color: '#a7f3d0',
  },
  searchCard: {
    display: 'grid',
    gap: '14px',
    padding: '24px',
    borderRadius: '24px',
    background: 'rgba(255,255,255,0.12)',
    border: '1px solid rgba(255,255,255,0.14)',
  },
  searchInputGroup: {
    display: 'flex',
    gap: '12px',
    alignItems: 'center',
  },
  searchInput: {
    flex: 1,
    minWidth: 0,
    padding: '16px 18px',
    borderRadius: '16px',
    border: '1px solid rgba(255,255,255,0.16)',
    background: 'rgba(255,255,255,0.12)',
    color: '#f8fafc',
    outline: 'none',
  },
  searchButton: {
    padding: '16px 24px',
    borderRadius: '16px',
    border: 'none',
    background: '#10b981',
    color: '#fff',
    fontWeight: 700,
    cursor: 'pointer',
  },
  location: {
    color: '#d1fae5',
    background: 'rgba(255,255,255,0.08)',
    borderRadius: '16px',
    padding: '14px 18px',
    maxWidth: '220px',
    fontWeight: 600,
  },
  statsCard: {
    display: 'grid',
    gap: '12px',
    padding: '22px',
    borderRadius: '24px',
    background: 'rgba(255,255,255,0.1)',
    border: '1px solid rgba(255,255,255,0.14)',
  },
  statsRow: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    color: '#f8fafc',
    fontWeight: 600,
  },
  rightPanel: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  heroCard: {
    width: '100%',
    minHeight: '420px',
    borderRadius: '32px',
    background: 'linear-gradient(180deg, rgba(8,64,46,0.92), rgba(13,84,62,0.98))',
    border: '1px solid rgba(16,185,129,0.3)',
    padding: '32px',
    display: 'grid',
    gap: '18px',
  },
  heroCardTitle: {
    fontSize: '2.25rem',
    fontWeight: 800,
    lineHeight: 1.05,
    color: '#f8fafc',
  },
  heroCardText: {
    color: 'rgba(248,250,252,0.82)',
    lineHeight: 1.7,
    fontSize: '1rem',
  },
  categorySection: {
    display: 'grid',
    gap: '16px',
    background: 'rgba(255,255,255,0.04)',
    border: '1px solid rgba(255,255,255,0.12)',
    borderRadius: '28px',
    padding: '28px',
  },
  categoryHeader: {
    fontSize: '1.25rem',
    fontWeight: 700,
    color: '#d1fae5',
  },
  categoryGrid: {
    display: 'grid',
    gap: '12px',
  },
  categoryItem: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '18px 22px',
    borderRadius: '18px',
    background: 'rgba(255,255,255,0.08)',
    color: '#f8fafc',
    fontWeight: 600,
  },
  arrow: {
    fontSize: '1.1rem',
    color: '#34d399',
  },
};
