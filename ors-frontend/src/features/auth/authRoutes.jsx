import { Navigate } from 'react-router-dom';
import { CandidateLoginPage } from './login/CandidateLoginPage.jsx';
import { RegisterPage } from './register/RegisterPage.jsx';
import { ForgotPasswordPage } from './forgot-password/ForgotPasswordPage.jsx';
import { ResetPasswordPage } from './reset-password/ResetPasswordPage.jsx';
import { ChangePasswordPage } from './change-password/ChangePasswordPage.jsx';
import { RecruiterLoginPage } from './login/RecruiterLoginPage.jsx';
import { RecruiterRegister } from './register/RecruiterRegister.jsx';

// Route dùng chung cho mọi actor, không nằm trong layout riêng của actor nào.
export const authRoutes = [
  { path: '/', element: <Navigate to="/login" replace /> },
  { path: '/login', element: <CandidateLoginPage /> },
  { path: '/register', element: <RegisterPage /> },
  { path: '/recruiter-login',element: <RecruiterLoginPage /> },
  { path: '/recruiter-register', element: <RecruiterRegister /> },
  { path: '/forgot-password', element: <ForgotPasswordPage /> },
  { path: '/reset-password', element: <ResetPasswordPage /> },
  { path: '/change-password', element: <ChangePasswordPage /> },
];
