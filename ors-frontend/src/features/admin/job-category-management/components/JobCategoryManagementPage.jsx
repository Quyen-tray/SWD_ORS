import { useJobCategoryManagement } from '../hooks/useJobCategoryManagement.js';

// <<boundary>> placeholder cho module job-category-management. Hiện thực chi tiết khi vào sprint tương ứng.
export function JobCategoryManagementPage() {
  const { items, isLoading } = useJobCategoryManagement();
  return (
    <div>
      <h2>JobCategoryManagement</h2>
      {isLoading ? <p>Đang tải...</p> : <pre>{JSON.stringify(items, null, 2)}</pre>}
    </div>
  );
}
