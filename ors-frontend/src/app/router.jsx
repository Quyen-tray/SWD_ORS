import { createBrowserRouter } from 'react-router-dom';
import { candidateRoutes } from '../features/candidate/candidateRoutes.jsx';
import { recruiterRoutes } from '../features/recruiter/recruiterRoutes.jsx';
import { moderatorRoutes } from '../features/moderator/moderatorRoutes.jsx';
import { adminRoutes } from '../features/admin/adminRoutes.jsx';
import { authRoutes } from '../features/auth/authRoutes.jsx';
import { publicRoutes } from '../features/public/publicRoutes.jsx';

// Mỗi mảng route con tương ứng với 1 "User Interaction Subsystem" (1 actor).
// Không có route nào ở đây chứa logic nghiệp vụ — chỉ điều phối tới đúng feature.
export const router = createBrowserRouter([
  ...publicRoutes,
  ...authRoutes,
  ...candidateRoutes,
  ...recruiterRoutes,
  ...moderatorRoutes,
  ...adminRoutes,
]);
