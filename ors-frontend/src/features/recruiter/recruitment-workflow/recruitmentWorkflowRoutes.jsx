import { PipelineBoard } from './components/PipelineBoard.jsx';

// UC-04 Update Pipeline Status - Kanban. PipelineBoard tự nạp dữ liệu qua
// usePipelineBoard() (React Query), page ở đây chỉ là khung tiêu đề.
function RecruitmentWorkflowPage() {
  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Pipeline tuyển dụng</h2>
      <PipelineBoard />
    </div>
  );
}

export const recruitmentWorkflowRoutes = [
  { path: 'recruitment-workflow', element: <RecruitmentWorkflowPage /> },
];