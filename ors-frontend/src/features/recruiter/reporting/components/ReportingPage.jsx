import { useReportingStats } from '../hooks/useReportingStats.js';

// <<boundary>> — UC-13/UC-14.
export function ReportingPage() {
  const { stats, isLoading } = useReportingStats({});
  return (
    <div>
      <h2>Thống kê tuyển dụng</h2>
      {isLoading ? <p>Đang tải...</p> : <pre>{JSON.stringify(stats, null, 2)}</pre>}
    </div>
  );
}
