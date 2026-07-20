package org.ors.subsystem.recruiter.recruitment_workflow.dto;

import java.time.Instant;

// Body của POST /interviews/{id}/reschedule (UC-05). Khớp
// pipelineApi.rescheduleInterview(interviewId, newTime) bên frontend, gửi lên đúng 1 field
// { scheduledTime } - xem ghi chú về định dạng ISO-8601 trong ScheduleInterviewRequest.
public record RescheduleInterviewRequest(Instant scheduledTime) {
}