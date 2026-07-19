package org.ors.subsystem.moderation.job_moderation.enums;

// Lý do đóng report bắt buộc phải chọn ở UC-48.
public enum ClosureReason {
    NO_VIOLATION_FOUND,
    DUPLICATE_REPORT,
    REPORTER_ERROR,
    ENTITY_ALREADY_REMOVED,
    OTHER
}
