package org.ors.subsystem.moderation.job_moderation.enums;

// Vòng đời của job_reports.status (UC-45..48). Cột DB là NVARCHAR tự do (không CHECK),
// nên enum này chỉ tồn tại ở tầng Java để trình biên dịch bắt lỗi gõ sai trạng thái.
public enum ReportStatus {
    PENDING,
    UNDER_INVESTIGATION,
    RESOLVED,
    CLOSED
}
