package org.ors.subsystem.recruiter.recruitment_workflow.controller;

import org.ors.subsystem.recruiter.recruitment_workflow.dto.InterviewResponse;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.RescheduleInterviewRequest;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.ScheduleInterviewRequest;
import org.ors.subsystem.recruiter.recruitment_workflow.service.IInterviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-05 Schedule/Reschedule/Cancel Interview (recruitment_workflow). Xem
// 00_KE_HOACH_TONG_QUAN.md để biết phạm vi hiện tại.
//
// Quy uoc:
//   - Path o day KHONG co /api/v1: context-path da khai bao trong application.properties.
//   - Path va HTTP method phai khop endpoints.js + pipelineApi.js ben frontend
//     (ENDPOINTS.interviews.schedule/detail/cancel/reschedule - schedule/cancel/reschedule
//     dung POST, khong phai PUT/DELETE, khop dung pipelineApi.scheduleInterview/
//     cancelInterview/rescheduleInterview).
//   - Controller chi anh xa HTTP sang service, KHONG chua business rule.
@RestController
@RequestMapping("/interviews")
public class InterviewController {

    private final IInterviewService interviewService;

    public InterviewController(IInterviewService interviewService) {
        this.interviewService = interviewService;
    }

    // UC-05 luồng chính - đặt lịch phỏng vấn mới.
    @PostMapping
    public InterviewResponse scheduleInterview(@RequestBody ScheduleInterviewRequest request) {
        return interviewService.scheduleInterview(request);
    }

    // Xem chi tiết 1 lịch phỏng vấn.
    @GetMapping("/{id}")
    public InterviewResponse getInterview(@PathVariable Integer id) {
        return interviewService.getInterview(id);
    }

    // UC-05 A2 - huỷ lịch đã đặt. Không nhận body (khớp
    // pipelineApi.cancelInterview(interviewId) bên frontend, không gửi gì trong
    // body).
    @PostMapping("/{id}/cancel")
    public InterviewResponse cancelInterview(@PathVariable Integer id) {
        return interviewService.cancelInterview(id);
    }

    // UC-05 A1 - đổi lịch đã đặt sang thời gian khác.
    @PostMapping("/{id}/reschedule")
    public InterviewResponse rescheduleInterview(@PathVariable Integer id,
            @RequestBody RescheduleInterviewRequest request) {
        return interviewService.rescheduleInterview(id, request);
    }
}