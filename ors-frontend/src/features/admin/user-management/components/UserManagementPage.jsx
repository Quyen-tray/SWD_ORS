import { useUserManagement } from '../hooks/useUserManagement.js';

// <<boundary>> placeholder cho module user-management. Hiện thực chi tiết khi vào sprint tương ứng.
export function UserManagementPage() {
  const { items, isLoading } = useUserManagement();
  return (
    <div>
      <h2>UserManagement</h2>
      {isLoading ? <p>Đang tải...</p> : <pre>{JSON.stringify(items, null, 2)}</pre>}
    </div>
  );
}
