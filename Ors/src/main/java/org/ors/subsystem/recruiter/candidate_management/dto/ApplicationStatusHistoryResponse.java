package org.ors.subsystem.recruiter.candidate_management.dto;

import org.ors.cross.share_kernel.entity.ApplicationStatusHistory;

import java.time.Instant;

// Một dòng lịch sử đổi trạng thái, cho GET /applications/{id}/status-history.
// changedByName có thể null: những dòng lịch sử được tạo trước khi luồng đăng nhập
// thật hoàn thiện (hoặc do migration/seed) có thể không có changed_by.
public record ApplicationStatusHistoryResponse(
        Integer id,
        String status,
        Instant changedAt,
        String reason,
        String changedByName
) {
    public static ApplicationStatusHistoryResponse from(ApplicationStatusHistory history) {
        return new ApplicationStatusHistoryResponse(
                history.getId(),
                history.getStatus(),
                history.getChangedAt(),
                history.getReason(),
                history.getChangedBy() == null ? null : history.getChangedBy().getEmail()
        );
    }
}