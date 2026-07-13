import { useState } from 'react';
import { useJobCategoryManagement } from '../hooks/useJobCategoryManagement.js';
import { Table } from '../../../../shared/components/Table.jsx';
import { Button } from '../../../../shared/components/Button.jsx';
import { Modal } from '../../../../shared/components/Modal.jsx';

// <<boundary>> — màn hình Quản lý danh mục ngành nghề (UC-57 tạo, UC-58 xem,
// UC-59 sửa, UC-60 xoá). Không chứa business rule: BR-14 do backend giữ.
export function JobCategoryManagementPage() {
  const {
    categories, isLoading, errorMessage, successMessage, clearMessages,
    createCategory, updateCategory, removeCategory,
  } = useJobCategoryManagement();

  const [newName, setNewName] = useState('');
  const [editing, setEditing] = useState(null);   // danh mục đang sửa
  const [editName, setEditName] = useState('');
  const [deleting, setDeleting] = useState(null); // danh mục đang chờ xác nhận xoá

  function submitCreate(e) {
    e.preventDefault();
    if (!newName.trim()) return;
    createCategory(newName.trim());
    setNewName('');
  }

  function submitEdit() {
    if (!editing || !editName.trim()) return;
    updateCategory(editing.id, editName.trim());
    setEditing(null);
  }

  function submitDelete() {
    if (!deleting) return;
    removeCategory(deleting.id);
    setDeleting(null);
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'categoryName', label: 'Tên danh mục' },
    {
      key: 'action',
      label: 'Hành động',
      render: (c) => (
        <div style={{ display: 'flex', gap: 8 }}>
          <Button
            variant="ghost"
            onClick={() => { clearMessages(); setEditing(c); setEditName(c.categoryName); }}
          >
            Sửa
          </Button>
          <Button variant="danger" onClick={() => { clearMessages(); setDeleting(c); }}>
            Xoá
          </Button>
        </div>
      ),
    },
  ];

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Quản lý danh mục ngành nghề</h2>

      <div style={styles.card}>
        <form style={styles.toolbar} onSubmit={submitCreate}>
          <input
            style={styles.input}
            placeholder="Tên danh mục mới..."
            value={newName}
            onChange={(e) => setNewName(e.target.value)}
          />
          <Button type="submit" variant="primary">Thêm danh mục</Button>
        </form>

        {errorMessage && <div style={styles.error}>{errorMessage}</div>}
        {successMessage && <div style={styles.success}>{successMessage}</div>}

        <p style={styles.muted}>
          {isLoading ? 'Đang tải...' : `Hiển thị ${categories.length} danh mục`}
        </p>

        {!isLoading && (
          <Table columns={columns} data={categories} emptyMessage="Chưa có danh mục nào." />
        )}
      </div>

      <Modal open={Boolean(editing)} onClose={() => setEditing(null)} title="Sửa danh mục">
        <input
          style={{ ...styles.input, width: '100%', marginBottom: 16 }}
          value={editName}
          onChange={(e) => setEditName(e.target.value)}
        />
        <div style={styles.modalActions}>
          <Button variant="ghost" onClick={() => setEditing(null)}>Huỷ</Button>
          <Button variant="primary" onClick={submitEdit}>Lưu</Button>
        </div>
      </Modal>

      <Modal open={Boolean(deleting)} onClose={() => setDeleting(null)} title="Xoá danh mục">
        <p style={styles.muted}>
          Xoá danh mục <strong>{deleting?.categoryName}</strong>?
        </p>
        <p style={styles.muted}>
          Danh mục đang được tin tuyển dụng sử dụng thì không xoá được.
        </p>
        <div style={styles.modalActions}>
          <Button variant="ghost" onClick={() => setDeleting(null)}>Huỷ</Button>
          <Button variant="danger" onClick={submitDelete}>Xoá</Button>
        </div>
      </Modal>
    </div>
  );
}

const styles = {
  card: { background: '#fff', borderRadius: 10, padding: 20, border: '1px solid #e5e7eb' },
  toolbar: { display: 'flex', gap: 8, alignItems: 'center', marginBottom: 12 },
  input: { padding: '8px 12px', borderRadius: 6, border: '1px solid #d1d5db', minWidth: 260 },
  muted: { color: '#6b7280', fontSize: 14 },
  modalActions: { display: 'flex', gap: 8, justifyContent: 'flex-end' },
  error: {
    background: '#fee2e2', color: '#991b1b', padding: '10px 12px',
    borderRadius: 6, marginBottom: 12, fontSize: 14,
  },
  success: {
    background: '#dcfce7', color: '#166534', padding: '10px 12px',
    borderRadius: 6, marginBottom: 12, fontSize: 14,
  },
};
