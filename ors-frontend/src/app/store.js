import { create } from 'zustand';

// Theo nguyên tắc Information Hiding (Ch04): chỉ đưa lên global store những gì
// thực sự dùng chung xuyên nhiều actor. State cục bộ của từng feature nên nằm
// trong hook riêng của feature đó (xem features/*/hooks), KHÔNG dồn hết vào đây.
export const useAuthStore = create((set) => ({
  user: null, // { id, email, role: 'CANDIDATE' | 'RECRUITER' | 'MODERATOR' | 'ADMIN' }
  token: null,
  isAuthenticated: false,

  setSession: (user, token) => set({ user, token, isAuthenticated: true }),
  clearSession: () => set({ user: null, token: null, isAuthenticated: false }),
}));

export const useNotificationStore = create((set) => ({
  unreadCount: 0,
  items: [], // realtime/polled notifications, dùng chung cho mọi actor (FE-05)

  setUnreadCount: (count) => set({ unreadCount: count }),
  addNotification: (notification) =>
    set((state) => ({
      items: [notification, ...state.items],
      unreadCount: state.unreadCount + 1,
    })),
  markAllRead: () => set({ unreadCount: 0 }),
}));
