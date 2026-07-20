package org.ors.subsystem.recruiter.recruitment_workflow.dto;

import java.time.Instant;

// Kết quả trả về sau khi đổi trạng thái (UC-04/UC-07). Frontend hiện tại chỉ
// invalidateQueries(['candidates']) rồi fetch lại danh sách (xem usePipelineStatus.js),
// không đọc field nào ở đây - nhưng trả đủ thông tin của lần đổi vừa xong thay vì 204
// No Content để component nào cần cập nhật lạc quan (optimistic update) sau này dùng
// được ngay, không phải sửa lại API.
public record ApplicationStatusResponse(
        Integer applicationId,
        String status,
        Instant changedAt,
        String reason
) {
}
