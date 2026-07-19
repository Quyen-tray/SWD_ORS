package org.ors.subsystem.moderation.job_moderation.dto;

// UC-49. Field null nghĩa là panel đó lỗi (EF-01) - các panel khác vẫn hiển thị bình thường.
public record DashboardSummaryResponse(
        Long openReportsCount,
        Double slaCompliancePercentToday,
        Long pendingCompanyVerificationCount,
        Long pendingJobPostingReviewCount
) {
}
