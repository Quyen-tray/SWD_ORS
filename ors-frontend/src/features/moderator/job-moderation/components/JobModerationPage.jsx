import { useJobModeration } from '../hooks/useJobModeration.js';

// <<boundary>> placeholder cho module job-moderation. Hiện thực chi tiết khi vào sprint tương ứng.
export function JobModerationPage() {
  const { items, isLoading } = useJobModeration();
  return (
    <div>
      <h2>JobModeration</h2>
      {isLoading ? <p>Đang tải...</p> : <pre>{JSON.stringify(items, null, 2)}</pre>}
    </div>
  );
}
