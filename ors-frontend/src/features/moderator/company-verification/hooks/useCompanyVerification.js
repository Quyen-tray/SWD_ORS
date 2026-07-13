import { useQuery } from '@tanstack/react-query';
import { CompanyVerificationApi } from '../api/company_verificationApi.js';

// <<control>> placeholder cho module company-verification.
export function useCompanyVerification() {
  const { data, isLoading } = useQuery({
    queryKey: ['company-verification'],
    queryFn: CompanyVerificationApi.list,
  });
  return { items: data ?? [], isLoading };
}
