package org.ors.subsystem.moderation.job_moderation.enums;

// 3 hành động xử lý report ở UC-47. Một report có thể chọn nhiều hành động cùng lúc (AF-01).
public enum EnforcementType {
    REMOVE_POSTING,
    SUSPEND_COMPANY,
    ISSUE_WARNING
}
