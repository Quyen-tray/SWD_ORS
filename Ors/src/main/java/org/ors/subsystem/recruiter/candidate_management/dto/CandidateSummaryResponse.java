package org.ors.subsystem.recruiter.candidate_management.dto;

import org.ors.cross.share_kernel.entity.JobApplication;

import java.math.BigDecimal;
import java.time.Instant;

// DTO cho một dòng trong danh sách ứng viên (UC-01).
// Không có thông tin nhạy cảm nào ngoài những gì màn hình danh sách cần hiển thị:
// tên, vị trí ứng tuyển, CV để tải, đánh giá (nếu đã có), trạng thái, ngày nộp.
public record CandidateSummaryResponse(
        Integer applicationId,
        String fullName,
        String jobTitle,
        Integer cvId,
        String cvName,
        BigDecimal rating,
        String status,
        Instant appliedAt) {
    public static CandidateSummaryResponse from(JobApplication application) {
        return new CandidateSummaryResponse(
                application.getId(),
                application.getCandidate().getFullName(),
                application.getJobPost().getTitle(),
                application.getCv().getId(),
                application.getCv().getCvName(),
                application.getRating(),
                application.getStatus(),
                application.getAppliedAt());
    }
}