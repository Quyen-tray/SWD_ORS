import { LoginPage } from './LoginPage.jsx';

export function CandidateLoginPage() {
  return (
    <LoginPage
      title="Đăng nhập ứng viên"
      subtitle="Chào mừng bạn đã quay trở lại. Đăng nhập để tìm việc phù hợp với bạn."
      imageSrc="/image/login.png"
      footerLink="Đăng ký ngay"
      footerHref="/register"
      roleHint="Đăng nhập với tư cách Ứng viên"
    />
  );
}
