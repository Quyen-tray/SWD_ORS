import { RecruiterLayout } from '../../layouts/RecruiterLayout.jsx';
import { CandidateManagementPage } from './candidate-management/components/CandidateManagementPage.jsx';
import { recruitmentWorkflowRoutes } from './recruitment-workflow/recruitmentWorkflowRoutes.jsx';
import { CommunicationPage } from './communication/components/CommunicationPage.jsx';
import { DashboardPage } from './dashboard/components/DashboardPage.jsx';
import { ReportingPage } from './reporting/components/ReportingPage.jsx';
import { JobManagementPage } from './job-management/components/JobManagementPage.jsx';

// UI Subsystem #2 — Recruiter. 5 module đã có sẵn HTML mockup gốc
// (candidate-management, recruitment-workflow, communication, dashboard, reporting)
// được scaffold đầy đủ 4 tầng (components/hooks/api/types).
export const recruiterRoutes = [
  {
    path: '/recruiter',
    element: <RecruiterLayout />,
    children: [
      {index: true, element: <DashboardPage /> },
      { path: 'candidates', element: <CandidateManagementPage /> },
      ...recruitmentWorkflowRoutes,
      { path: 'communication', element: <CommunicationPage /> },
      { path: 'reporting', element: <ReportingPage /> },
      { path: 'jobs', element: <JobManagementPage /> },
    ],
  },
];
