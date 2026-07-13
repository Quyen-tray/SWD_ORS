import { PipelineBoard } from './components/PipelineBoard.jsx';

// Wrapper page giữ dữ liệu applications cấp route (nạp qua React Query trong thực tế).
function RecruitmentWorkflowPage() {
  return (
    <div>
      <h2>Recruitment Workflow</h2>
      <PipelineBoard applications={[]} />
    </div>
  );
}

export const recruitmentWorkflowRoutes = [
  { path: 'recruitment-workflow', element: <RecruitmentWorkflowPage /> },
];
