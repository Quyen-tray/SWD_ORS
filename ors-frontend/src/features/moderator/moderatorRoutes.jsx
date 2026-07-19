import { ModeratorLayout } from '../../layouts/ModeratorLayout.jsx';
import { CompanyVerificationPage } from './company-verification/components/CompanyVerificationPage.jsx';
import { JobModerationPage } from './job-moderation/components/JobModerationPage.jsx';
import { ModerationDashboardPage } from './dashboard/components/ModerationDashboardPage.jsx';
import { ReportQueuePage } from './report-management/components/ReportQueuePage.jsx';
import { ReportDetailPage } from './report-management/components/ReportDetailPage.jsx';
import { AuditLogPage } from './audit-log/components/AuditLogPage.jsx';

// UI Subsystem #3 — Moderator.
export const moderatorRoutes = [
  {
    path: '/moderator',
    element: <ModeratorLayout />,
    children: [
      { index: true, element: <JobModerationPage /> },
      { path: 'company-verification', element: <CompanyVerificationPage /> },
      { path: 'job-moderation', element: <JobModerationPage /> },
      // UC-45..50 — Report Moderation (Slice B).
      { path: 'dashboard', element: <ModerationDashboardPage /> },
      { path: 'reports', element: <ReportQueuePage /> },
      { path: 'reports/:id', element: <ReportDetailPage /> },
      { path: 'audit-log', element: <AuditLogPage /> },
    ],
  },
];
