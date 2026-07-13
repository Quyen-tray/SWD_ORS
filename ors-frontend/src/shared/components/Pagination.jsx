// <<boundary>> dùng chung.
export function Pagination({ page, totalPages, onChange }) {
  if (totalPages <= 1) return null;
  return (
    <div style={{ display: 'flex', gap: 8, marginTop: 12 }}>
      <button disabled={page <= 1} onClick={() => onChange(page - 1)}>Trước</button>
      <span>Trang {page}/{totalPages}</span>
      <button disabled={page >= totalPages} onClick={() => onChange(page + 1)}>Sau</button>
    </div>
  );
}
