import { useState } from 'react';
import { useUserManagement } from '../hooks/useUserManagement.js';
import { useAuthStore } from '../../../../app/store.js';
import { Table } from '../../../../shared/components/Table.jsx';
import { Button } from '../../../../shared/components/Button.jsx';
import { Modal } from '../../../../shared/components/Modal.jsx';

function formatCreatedDate(isoString) {
  if (!isoString) return '';
  return new Date(isoString).toLocaleDateString('vi-VN');
}

// <<boundary>> — màn hình Quản lý người dùng (UC-53 xem/tìm/lọc, UC-54 kích hoạt,
// UC-55 tạm ngưng, UC-56 cấm). Không chứa business rule: mọi luật của BR-13 do backend
// quyết định, màn hình chỉ hiển thị lại câu từ chối mà backend trả về.
//
// Chưa có luồng đăng nhập nên store chưa có user. Tạm coi admin@ors.vn là người đang
// đăng nhập, khớp với fallback phía backend (UserService.currentAdmin). Khi login xong thì
// bỏ hằng số này, phần còn lại giữ nguyên.
const FALLBACK_ADMIN_EMAIL = 'admin@ors.vn';

const ROLES = ['CANDIDATE', 'RECRUITER', 'MODERATOR', 'ADMIN'];
const STATUSES = ['ACTIVE', 'INACTIVE', 'BANNED'];

export function UserManagementPage() {
  const {
    users, isLoading,
    keyword, setKeyword, role, setRole, status, setStatus, resetFilters,
    errorMessage, setErrorMessage,
    activateUser, deactivateUser, banUser,
  } = useUserManagement();

  const currentUser = useAuthStore((s) => s.user);
  const currentEmail = currentUser?.email ?? FALLBACK_ADMIN_EMAIL;

  // Hộp thoại nhập lý do dùng chung cho cả Tạm ngưng và Cấm: BR-13 bắt buộc ghi lý do,
  // nên không cho bấm thẳng mà phải qua bước nhập lý do.
  const [confirm, setConfirm] = useState(null); // { user, action: 'deactivate' | 'ban' }
  const [reason, setReason] = useState('');

  function openConfirm(user, action) {
    setConfirm({ user, action });
    setReason('');
    setErrorMessage('');
  }

  function submitConfirm() {
    if (!confirm) return;
    if (confirm.action === 'ban') banUser(confirm.user.id, reason);
    else deactivateUser(confirm.user.id, reason);
    setConfirm(null);
  }

  const columns = [
    {
      key: 'email',
      label: 'Email',
      render: (u) => (
        <span>
          {u.email}
          {u.email === currentEmail && <span style={styles.you}>Bạn</span>}
        </span>
      ),
    },
    { key: 'role', label: 'Vai trò', render: (u) => <span style={styles.rolePill}>{u.role}</span> },
    {
      key: 'status',
      label: 'Trạng thái',
      render: (u) => <span style={statusPill(u.status)}>{STATUS_LABEL[u.status] ?? u.status}</span>,
    },
    // Không dùng shared formatDate ở đây: nó trả cả giờ:phút:giây, mà cột này chỉ cần ngày.
    { key: 'createdAt', label: 'Ngày tạo', render: (u) => formatCreatedDate(u.createdAt) },
    {
      key: 'action',
      label: 'Hành động',
      render: (u) => {
        // Admin không được tự tạm ngưng / tự cấm chính mình (BR-13). Backend chặn thật;
        // ẩn nút ở đây chỉ để người dùng khỏi bấm vào thứ chắc chắn bị từ chối.
        if (u.email === currentEmail) return <span style={styles.muted}>—</span>;
        return (
          <div style={{ display: 'flex', gap: 8 }}>
            {u.status !== 'ACTIVE' && (
              <Button variant="ghost" onClick={() => activateUser(u.id)}>Kích hoạt</Button>
            )}
            {u.status === 'ACTIVE' && (
              <Button variant="ghost" onClick={() => openConfirm(u, 'deactivate')}>Tạm ngưng</Button>
            )}
            {u.status !== 'BANNED' && (
              <Button variant="danger" onClick={() => openConfirm(u, 'ban')}>Cấm</Button>
            )}
          </div>
        );
      },
    },
  ];

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Quản lý người dùng</h2>

      <div style={styles.card}>
        <div style={styles.toolbar}>
          <input
            style={styles.input}
            placeholder="Tìm theo email..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <select style={styles.select} value={role} onChange={(e) => setRole(e.target.value)}>
            <option value="">Vai trò: Tất cả</option>
            {ROLES.map((r) => <option key={r} value={r}>{r}</option>)}
          </select>
          <select style={styles.select} value={status} onChange={(e) => setStatus(e.target.value)}>
            <option value="">Trạng thái: Tất cả</option>
            {STATUSES.map((s) => <option key={s} value={s}>{STATUS_LABEL[s]}</option>)}
          </select>
          <Button variant="ghost" onClick={resetFilters}>Đặt lại</Button>
        </div>

        {errorMessage && <div style={styles.error}>{errorMessage}</div>}

        <p style={styles.muted}>
          {isLoading ? 'Đang tải...' : `Hiển thị ${users.length} người dùng`}
        </p>

        {!isLoading && (
          <Table columns={columns} data={users} emptyMessage="Không tìm thấy người dùng nào." />
        )}
      </div>

      <Modal
        open={Boolean(confirm)}
        onClose={() => setConfirm(null)}
        title={confirm?.action === 'ban' ? 'Cấm tài khoản' : 'Tạm ngưng tài khoản'}
      >
        <p style={styles.muted}>
          Tài khoản: <strong>{confirm?.user.email}</strong>
        </p>
        <p style={styles.muted}>
          {confirm?.action === 'ban'
            ? 'Cấm là biện pháp áp dụng khi người dùng vi phạm chính sách. Bắt buộc ghi lý do.'
            : 'Tạm ngưng là biện pháp tạm thời, có thể kích hoạt lại. Bắt buộc ghi lý do.'}
        </p>
        <input
          style={{ ...styles.input, width: '100%', marginBottom: 16 }}
          placeholder="Nhập lý do..."
          value={reason}
          onChange={(e) => setReason(e.target.value)}
        />
        <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
          <Button variant="ghost" onClick={() => setConfirm(null)}>Huỷ</Button>
          <Button
            variant={confirm?.action === 'ban' ? 'danger' : 'primary'}
            onClick={submitConfirm}
          >
            Xác nhận
          </Button>
        </div>
      </Modal>
    </div>
  );
}

const STATUS_LABEL = {
  ACTIVE: 'Đang hoạt động',
  INACTIVE: 'Tạm ngưng',
  BANNED: 'Bị cấm',
  EMAIL_PENDING: 'Chờ xác thực',
  LOCKED: 'Bị khoá',
};

const STATUS_COLOR = {
  ACTIVE: { background: '#dcfce7', color: '#166534' },
  INACTIVE: { background: '#fef3c7', color: '#92400e' },
  BANNED: { background: '#fee2e2', color: '#991b1b' },
  EMAIL_PENDING: { background: '#e0e7ff', color: '#3730a3' },
  LOCKED: { background: '#e5e7eb', color: '#374151' },
};

const pillBase = {
  display: 'inline-block',
  padding: '2px 10px',
  borderRadius: 999,
  fontSize: 12,
  fontWeight: 600,
};

function statusPill(status) {
  return { ...pillBase, ...(STATUS_COLOR[status] ?? STATUS_COLOR.LOCKED) };
}

const styles = {
  card: { background: '#fff', borderRadius: 10, padding: 20, border: '1px solid #e5e7eb' },
  toolbar: { display: 'flex', gap: 8, alignItems: 'center', flexWrap: 'wrap', marginBottom: 12 },
  input: { padding: '8px 12px', borderRadius: 6, border: '1px solid #d1d5db', minWidth: 260 },
  select: { padding: '8px 12px', borderRadius: 6, border: '1px solid #d1d5db' },
  rolePill: { ...pillBase, background: '#f1f5f9', color: '#334155' },
  you: { ...pillBase, background: '#dcfce7', color: '#166534', marginLeft: 8 },
  muted: { color: '#6b7280', fontSize: 14 },
  error: {
    background: '#fee2e2',
    color: '#991b1b',
    padding: '10px 12px',
    borderRadius: 6,
    marginBottom: 12,
    fontSize: 14,
  },
};
