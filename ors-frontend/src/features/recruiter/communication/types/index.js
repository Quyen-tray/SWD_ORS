/**
 * UC-08/09/10 (Send Message/Notification/Email) theo số UC đã đánh lại.
 * Quy ước: Email dùng subject nhúng đầu message dạng "[Subject: ...]\n<nội dung>"
 * (xem ghi chú trong Use_Case_Specification_no_removed_renumbered.docx).
 * @typedef {Object} CommunicationItem
 * @property {number} id
 * @property {'MESSAGE'|'EMAIL'} type — suy luận từ pattern "[Subject: ...]", không phải cột DB.
 * @property {string} message
 * @property {string} sentAt
 */
