package org.ors.subsystem.moderation.job_moderation.enums;

// audit_logs không có cột entity_type/entity_id riêng (khác migration cá nhân trước đây),
// nên giá trị này được nhồi vào audit_logs.description qua AuditDescriptionCodec
// thay vì thêm cột mới vào bảng dùng chung.
public enum EntityType {
    JOB_POST,
    COMPANY,
    REPORT
}
