// <<boundary>> dùng chung. Component thuần trình bày, không gọi API.
export function Button({ children, variant = 'primary', ...props }) {
  const base = {
    padding: '8px 16px',
    borderRadius: 6,
    border: 'none',
    cursor: 'pointer',
    fontWeight: 600,
  };
  const variants = {
    primary: { background: '#2563eb', color: '#fff' },
    danger: { background: '#dc2626', color: '#fff' },
    ghost: { background: 'transparent', color: '#2563eb' },
  };
  return (
    <button style={{ ...base, ...variants[variant] }} {...props}>
      {children}
    </button>
  );
}
