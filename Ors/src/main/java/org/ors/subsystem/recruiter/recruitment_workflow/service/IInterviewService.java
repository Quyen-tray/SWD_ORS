package org.ors.subsystem.recruiter.recruitment_workflow.service;

import org.ors.subsystem.recruiter.recruitment_workflow.dto.InterviewResponse;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.RescheduleInterviewRequest;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.ScheduleInterviewRequest;

// UC-05 Schedule/Reschedule/Cancel Interview (recruitment_workflow - xem
// 00_KE_HOACH_TONG_QUAN.md để biết phạm vi hiện tại: UC-01, UC-04, UC-05, UC-06, UC-07).
//
// Controller phụ thuộc vào interface này chứ không phụ thuộc vào lớp hiện thực, cùng quy
// ước với IApplicationStatusService (UC-04) trong package này.
public interface IInterviewService {

    // UC-05 luồng chính: đặt lịch phỏng vấn mới cho 1 đơn ứng tuyển. round tự tính (số
    // interview đã có của đơn này + 1), không nhận từ client.
    InterviewResponse scheduleInterview(ScheduleInterviewRequest request);

    // Xem chi tiết 1 lịch phỏng vấn - chỉ trong công ty của Recruiter đang đăng nhập.
    InterviewResponse getInterview(Integer interviewId);

    // UC-05 A2: huỷ lịch đã đặt. Không cho huỷ lịch đã CANCELLED/COMPLETED.
    InterviewResponse cancelInterview(Integer interviewId);

    // UC-05 A1: đổi lịch đã đặt sang thời gian khác, giữ nguyên round. Không cho đổi lịch
    // đã CANCELLED/COMPLETED.
    InterviewResponse rescheduleInterview(Integer interviewId, RescheduleInterviewRequest request);
}