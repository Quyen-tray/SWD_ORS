package org.ors.subsystem.moderation.job_moderation.dto;

import java.time.Instant;

// UC-45 - một dòng trong report queue.
public record ReportSummaryResponse(
        Integer id,
        Integer jobPostId,
        String jobPostTitle,
        String reporterEmail,
        Instant createdAt,
        String status,
        boolean slaOverdue
) {
}
