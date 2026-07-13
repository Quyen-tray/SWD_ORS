import { useDashboardSummary } from '../hooks/useDashboardSummary.js';

// <<boundary>> — UC-12 View Recruitment Dashboard.
export function DashboardPage() {
  const { summary, isLoading } = useDashboardSummary();
  return (
    <div>
      <h2>Dashboard</h2>
      {isLoading ? <p>Đang tải...</p> : <pre>{JSON.stringify(summary, null, 2)}</pre>}
    </div>
  );
}
