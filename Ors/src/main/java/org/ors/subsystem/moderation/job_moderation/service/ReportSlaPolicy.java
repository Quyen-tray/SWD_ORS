package org.ors.subsystem.moderation.job_moderation.service;

import org.ors.cross.share_kernel.entity.JobReport;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

// BR-06: report phải xử lý trong 24h. Dùng lại ở cả ReportService (cột slaOverdue trên
// từng dòng queue) và ModerationDashboardService (Facade, % tuân thủ SLA) - một chỗ duy
// nhất định nghĩa "quá hạn" nghĩa là gì.
public final class ReportSlaPolicy {

    private static final long SLA_HOURS = 24;

    private ReportSlaPolicy() {
    }

    public static boolean isOverdue(JobReport report) {
        boolean open = ReportStatus.PENDING.name().equals(report.getStatus())
                || ReportStatus.UNDER_INVESTIGATION.name().equals(report.getStatus());
        return open && report.getCreatedAt() != null
                && report.getCreatedAt().plus(SLA_HOURS, ChronoUnit.HOURS).isBefore(Instant.now());
    }
}
