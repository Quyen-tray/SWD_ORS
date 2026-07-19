package org.ors.subsystem.moderation.job_moderation.dto;

import java.time.Instant;
import java.util.List;

// UC-46.
public record ReportDetailResponse(
        Integer id,
        String reason,
        String status,
        Instant createdAt,
        boolean slaOverdue,
        JobPostSummary reportedEntity,
        boolean entityMissing,
        List<AuditLogResponse> moderationHistory
) {
    public record JobPostSummary(Integer id, String title, String description) {
    }
}
