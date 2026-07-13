import { LoginPage } from './login/LoginPage.jsx';
import { RegisterPage } from './register/RegisterPage.jsx';
import { ForgotPasswordPage } from './forgot-password/ForgotPasswordPage.jsx';
import { ResetPasswordPage } from './reset-password/ResetPasswordPage.jsx';
import { ChangePasswordPage } from './change-password/ChangePasswordPage.jsx';

// Route dùng chung cho mọi actor, không nằm trong layout riêng của actor nào.
export const authRoutes = [
  { path: '/login', element: <LoginPage /> },
  { path: '/register', element: <RegisterPage /> },
  { path: '/forgot-password', element: <ForgotPasswordPage /> },
  { path: '/reset-password', element: <ResetPasswordPage /> },
  { path: '/change-password', element: <ChangePasswordPage /> },
];
