import { useCompanyVerification } from '../hooks/useCompanyVerification.js';

// <<boundary>> placeholder cho module company-verification. Hiện thực chi tiết khi vào sprint tương ứng.
export function CompanyVerificationPage() {
  const { items, isLoading } = useCompanyVerification();
  return (
    <div>
      <h2>CompanyVerification</h2>
      {isLoading ? <p>Đang tải...</p> : <pre>{JSON.stringify(items, null, 2)}</pre>}
    </div>
  );
}
