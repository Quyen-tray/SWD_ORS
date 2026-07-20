package org.ors.subsystem.recruiter.recruitment_workflow.dto;

import org.ors.cross.share_kernel.entity.Interview;

import java.math.BigDecimal;
import java.time.Instant;

// Kết quả trả về cho cả 5 endpoint của UC-05 + UC-06 (schedule/detail/cancel/reschedule/
// evaluation) - 1 DTO dùng chung, ánh xạ gần như 1-1 từ entity Interview. Từ Phase 4a,
// outcome/rating/comments được InterviewService.recordOutcome() ghi thật (trước đó luôn
// null vì UC-06 chưa code) - Phase 4b dùng lại đúng DTO này cho màn hình chi tiết/kết quả,
// không phải thêm response mới.
public record InterviewResponse(
        Integer id,
        Integer applicationId,
        Instant scheduledTime,
        String location,
        String meetingLink,
        String status,
        Integer round,
        String outcome,
        BigDecimal rating,
        String comments
) {
    public static InterviewResponse from(Interview interview) {
        return new InterviewResponse(
                interview.getId(),
                interview.getApplication().getId(),
                interview.getScheduledTime(),
                interview.getLocation(),
                interview.getMeetingLink(),
                interview.getStatus(),
                interview.getRound(),
                interview.getOutcome(),
                interview.getRating(),
                interview.getComments());
    }
}