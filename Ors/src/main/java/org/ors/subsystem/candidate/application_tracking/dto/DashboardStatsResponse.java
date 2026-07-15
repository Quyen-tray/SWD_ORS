package org.ors.subsystem.candidate.application_tracking.dto;

// UC-73: DTO chứa thống kê tổng quan cho Candidate Dashboard.
// Tài liệu yêu cầu: Total Applied, In Progress, Interviews Scheduled, Saved Jobs, Profile Completion.
public record DashboardStatsResponse(
        long totalApplications,
        long pendingInterviews,
        long savedJobs,
        int profileCompletionPercentage
) {
}
