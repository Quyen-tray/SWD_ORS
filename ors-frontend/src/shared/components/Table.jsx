// <<boundary>> dùng chung: bảng generic, feature truyền columns + data vào.
export function Table({ columns, data, emptyMessage = 'Không có dữ liệu.' }) {
  if (!data?.length) return <p>{emptyMessage}</p>;
  return (
    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
      <thead>
        <tr>
          {columns.map((col) => (
            <th key={col.key} style={thStyle}>{col.label}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.map((row, i) => (
          <tr key={row.id ?? i}>
            {columns.map((col) => (
              <td key={col.key} style={tdStyle}>
                {col.render ? col.render(row) : row[col.key]}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}

const thStyle = { textAlign: 'left', borderBottom: '2px solid #e5e7eb', padding: 8 };
const tdStyle = { borderBottom: '1px solid #f1f5f9', padding: 8 };
