package org.ors.subsystem.recruiter.recruitment_workflow.dto;

import org.ors.cross.share_kernel.entity.Interview;

import java.math.BigDecimal;
import java.time.Instant;

// Kết quả trả về cho cả 4 endpoint của UC-05 (schedule/detail/cancel/reschedule) - 1 DTO
// dùng chung, ánh xạ gần như 1-1 từ entity Interview. outcome/rating/comments luôn null ở
// Phase 3a (chưa có UC-06/Phase 4a nào ghi vào 3 cột này) nhưng vẫn trả ra để Phase 3b/4b
// dùng lại đúng DTO này cho màn hình chi tiết, không phải thêm response mới.
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