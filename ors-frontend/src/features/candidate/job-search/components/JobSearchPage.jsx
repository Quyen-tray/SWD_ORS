import { useJobSearch } from '../hooks/useJobSearch.js';

// <<boundary>> — UC: Search Jobs (FE-03).
export function JobSearchPage() {
  const { jobs, isLoading, keyword, setKeyword } = useJobSearch();

  return (
    <div>
      <h2>Tìm việc làm</h2>
      <input
        placeholder="Tìm theo tên công việc..."
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
      />
      {isLoading ? <p>Đang tải...</p> : <ul>{jobs.map((j) => <li key={j.id}>{j.title}</li>)}</ul>}
    </div>
  );
}
