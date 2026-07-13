import { useCommunicationHistory } from '../hooks/useCommunicationHistory.js';

// <<boundary>> — UC-11 View Communication History.
export function CommunicationPage({ applicationId }) {
  const { items, isLoading } = useCommunicationHistory(applicationId);

  return (
    <div>
      <h2>Lịch sử trao đổi</h2>
      {isLoading ? (
        <p>Đang tải...</p>
      ) : (
        <ul>
          {items.map((item) => (
            <li key={item.id}>
              <strong>[{item.type}]</strong> {item.subject ? `${item.subject} — ` : ''}
              {item.body}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
