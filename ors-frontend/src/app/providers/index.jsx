import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 30_000,
      retry: 1,
    },
  },
});

// Nơi duy nhất gộp các provider toàn cục (React Query, Theme, v.v.).
// Thêm provider mới ở đây, không rải rác trong từng feature.
export function AppProviders({ children }) {
  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
}
