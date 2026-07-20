package org.ors.subsystem.administration.audit.dto;

import org.ors.cross.share_kernel.entity.AuditLog;

import java.time.Instant;

// DTO cho màn hình xem nhật ký kiểm toán (UC-61).
// Chỉ lấy id và email của actor, KHÔNG nhúng cả entity User (tránh lộ passwordHash).
public record AuditLogResponse(
        Integer id,
        Integer userId,
        String userEmail,
        String actionType,
        String description,
        Instant createdAt
) {
    public static AuditLogResponse from(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getUser().getId(),
                log.getUser().getEmail(),
                log.getActionType(),
                log.getDescription(),
                log.getCreatedAt()
        );
    }
}
