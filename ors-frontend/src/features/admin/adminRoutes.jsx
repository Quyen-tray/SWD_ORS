import { AdminLayout } from '../../layouts/AdminLayout.jsx';
import { UserManagementPage } from './user-management/components/UserManagementPage.jsx';
import { JobCategoryManagementPage } from './job-category-management/components/JobCategoryManagementPage.jsx';

// UI Subsystem #4 — Admin.
export const adminRoutes = [
  {
    path: '/admin',
    element: <AdminLayout />,
    children: [
      { index: true, element: <UserManagementPage /> },
      { path: 'users', element: <UserManagementPage /> },
      { path: 'job-categories', element: <JobCategoryManagementPage /> },
    ],
  },
];
