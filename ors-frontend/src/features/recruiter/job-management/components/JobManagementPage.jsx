import { useJobManagement } from '../hooks/useJobManagement.js';

export function JobManagementPage() {
  const { jobs, isLoading } = useJobManagement();
  return (
    <div>
      <h2>Quản lý tin tuyển dụng</h2>
      {isLoading ? <p>Đang tải...</p> : <ul>{jobs.map((j) => <li key={j.id}>{j.title}</li>)}</ul>}
    </div>
  );
}
