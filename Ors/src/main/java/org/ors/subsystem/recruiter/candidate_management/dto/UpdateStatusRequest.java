package org.ors.subsystem.recruiter.candidate_management.dto;

// Body của PATCH /applications/{id}/status (UC-04, tái dùng ở UC-07). Khớp đúng
// pipelineApi.updateStatus() bên frontend (recruitment-workflow/api/pipelineApi.js):
// { status, reason }. status là trạng thái ĐÍCH mà Recruiter muốn chuyển tới - service
// tự suy ra đó là "advance" hay "reject" (xem ApplicationStatusService), không phải
// một action riêng do client chỉ định. reason bắt buộc khi status = "REJECTED"
// (Alternative Flow A1/A2 của UC-04).
public record UpdateStatusRequest(String status, String reason) {
}