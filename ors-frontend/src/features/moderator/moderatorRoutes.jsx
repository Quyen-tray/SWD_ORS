import { ModeratorLayout } from '../../layouts/ModeratorLayout.jsx';
import { CompanyVerificationPage } from './company-verification/components/CompanyVerificationPage.jsx';
import { JobModerationPage } from './job-moderation/components/JobModerationPage.jsx';

// UI Subsystem #3 — Moderator.
export const moderatorRoutes = [
  {
    path: '/moderator',
    element: <ModeratorLayout />,
    children: [
      { index: true, element: <JobModerationPage /> },
      { path: 'company-verification', element: <CompanyVerificationPage /> },
      { path: 'job-moderation', element: <JobModerationPage /> },
    ],
  },
];
