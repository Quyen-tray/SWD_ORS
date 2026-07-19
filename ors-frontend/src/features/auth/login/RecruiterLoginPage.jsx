import { LoginPage } from './LoginPage.jsx';

export function RecruiterLoginPage() {
  return (
    <LoginPage
      title="Đăng nhập nhà tuyển dụng"
      subtitle="Đăng nhập để quản lý tuyển dụng và tìm kiếm ứng viên phù hợp." 
      imageSrc="/image/login_nha_tuyen_dung.png"
      footerLink="Đăng ký ngay"
      footerHref="/recruiter-register"
      roleHint="Đăng nhập với tư cách Nhà tuyển dụng"
    />
  );
}
