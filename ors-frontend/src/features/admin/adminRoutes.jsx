import { AdminLayout } from '../../layouts/AdminLayout.jsx';
import { AdminDashboardPage } from './dashboard/components/AdminDashboardPage.jsx';
import { UserManagementPage } from './user-management/components/UserManagementPage.jsx';
import { UserDetailPage } from './user-detail/components/UserDetailPage.jsx';
import { JobCategoryManagementPage } from './job-category-management/components/JobCategoryManagementPage.jsx';
import { AuditLogPage } from './audit-log/components/AuditLogPage.jsx';

// UI Subsystem #4 — Admin.
export const adminRoutes = [
  {
    path: '/admin',
    element: <AdminLayout />,
    children: [
      { index: true, element: <AdminDashboardPage /> },
      { path: 'dashboard', element: <AdminDashboardPage /> },
      { path: 'users', element: <UserManagementPage /> },
      { path: 'users/:id', element: <UserDetailPage /> },
      { path: 'job-categories', element: <JobCategoryManagementPage /> },
      { path: 'audit-logs', element: <AuditLogPage /> },
    ],
  },
];
