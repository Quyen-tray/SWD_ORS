package org.ors.subsystem.moderation.job_moderation.dto;

import java.time.Instant;

// UC-50. entityType/entityId có thể null nếu dòng audit cũ (trước Slice B, hoặc do module
// khác ghi) không theo định dạng "[entityType=X;entityId=Y] ..." - xem AuditDescriptionCodec.
public record AuditLogResponse(
        Integer id,
        Instant createdAt,
        Integer moderatorId,
        String actionType,
        String entityType,
        Integer entityId,
        String description
) {
}
