import { CandidateLayout } from '../../layouts/CandidateLayout.jsx';
import { jobSearchRoutes } from './job-search/jobSearchRoutes.jsx';
import { ApplicationTrackingPage } from './application-tracking/components/ApplicationTrackingPage.jsx';
import { CvManagementPage } from './cv-management/components/CvManagementPage.jsx';

// UI Subsystem #1 — Candidate. Toàn bộ route con nằm trong CandidateLayout.
export const candidateRoutes = [
  {
    path: '/candidate',
    element: <CandidateLayout />,
    children: [
      ...jobSearchRoutes,
      { path: 'applications', element: <ApplicationTrackingPage /> },
      { path: 'cvs', element: <CvManagementPage /> },
    ],
  },
];
