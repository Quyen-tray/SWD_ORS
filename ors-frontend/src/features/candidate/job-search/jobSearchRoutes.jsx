import { JobSearchPage } from './components/JobSearchPage.jsx';

export const jobSearchRoutes = [
  { index: true, element: <JobSearchPage /> },
  { path: 'jobs', element: <JobSearchPage /> },
];
