export function formatDate(isoString) {
  if (!isoString) return '';
  return new Date(isoString).toLocaleString('vi-VN');
}
