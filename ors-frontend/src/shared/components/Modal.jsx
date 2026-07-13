// <<boundary>> dùng chung: modal khung rỗng, feature tự truyền nội dung vào.
export function Modal({ open, onClose, title, children }) {
  if (!open) return null;
  return (
    <div style={overlayStyle} onClick={onClose}>
      <div style={panelStyle} onClick={(e) => e.stopPropagation()}>
        <h3>{title}</h3>
        {children}
      </div>
    </div>
  );
}

const overlayStyle = {
  position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
  display: 'flex', alignItems: 'center', justifyContent: 'center',
};
const panelStyle = {
  background: '#fff', borderRadius: 8, padding: 24, minWidth: 360,
};
