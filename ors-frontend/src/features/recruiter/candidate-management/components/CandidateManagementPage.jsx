import { useCandidateList } from '../hooks/useCandidateList.js';
import { Table } from '../../../../shared/components/Table.jsx';
import { Pagination } from '../../../../shared/components/Pagination.jsx';

// <<boundary>> — Coordinator cho UC-01/UC-02/UC-03 (View List / View Profile / Export).
export function CandidateManagementPage() {
  const { candidates, isLoading, filters, setFilters } = useCandidateList();

  const columns = [
    { key: 'fullName', label: 'Ứng viên' },
    { key: 'jobTitle', label: 'Vị trí ứng tuyển' },
    { key: 'status', label: 'Trạng thái' },
  ];

  return (
    <div>
      <h2>Quản lý ứng viên</h2>
      {isLoading ? <p>Đang tải...</p> : <Table columns={columns} data={candidates} />}
      <Pagination
        page={filters.page}
        totalPages={1}
        onChange={(page) => setFilters((f) => ({ ...f, page }))}
      />
    </div>
  );
}
