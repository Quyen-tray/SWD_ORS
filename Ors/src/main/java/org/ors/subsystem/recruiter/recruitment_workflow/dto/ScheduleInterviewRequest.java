package org.ors.subsystem.recruiter.recruitment_workflow.dto;

import java.time.Instant;

// Body của POST /interviews (UC-05 Schedule Interview). Khớp pipelineApi.scheduleInterview()
// bên frontend (recruitment-workflow/api/pipelineApi.js) - payload hiện tại của scaffold
// InterviewModal.jsx chỉ có { applicationId, scheduledTime }, location/meetingLink để trống
// khi chưa nhập. Phase 3b (viết lại InterviewModal.jsx theo frontend_demo/uc05-schedule-
// interview.html) cần thêm 2 field này vào form, cùng với round chỉ để đọc (server tự tính).
//
// LƯU Ý cho Phase 3b: scheduledTime phải là chuỗi ISO-8601 CÓ múi giờ/offset (vd chuỗi trả
// về từ `new Date(...).toISOString()`), không phải giá trị thô của <input type="datetime-
// local"> (dạng "2026-07-20T14:30", không có "Z"/offset) - Jackson không parse được thành
// Instant nếu thiếu offset, request sẽ lỗi 400 ở tầng Spring trước khi vào tới service.
public record ScheduleInterviewRequest(
        Integer applicationId,
        Instant scheduledTime,
        String location,
        String meetingLink
) {
}