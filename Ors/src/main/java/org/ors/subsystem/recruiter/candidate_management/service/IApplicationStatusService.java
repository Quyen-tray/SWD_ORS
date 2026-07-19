package org.ors.subsystem.recruiter.candidate_management.service;

import org.ors.subsystem.recruiter.candidate_management.dto.ApplicationStatusHistoryResponse;
import org.ors.subsystem.recruiter.candidate_management.dto.ApplicationStatusResponse;

import java.util.List;

// UC-04 Update Pipeline Status (tái dùng ở UC-07 Hire/Offer/Reject - xem
// 00_KE_HOACH_TONG_QUAN.md mục 2.2). Controller phụ thuộc interface này, không phụ
// thuộc lớp hiện thực - giống mọi service khác trong project.
public interface IApplicationStatusService {

    // UC-04 luồng chính + A1 (Reject). status là trạng thái ĐÍCH; service tự xác định
    // đó là advance() hay reject() dựa trên state hiện tại (xem package .state), rồi
    // ghi 1 dòng application_status_histories. reason bắt buộc khi target = REJECTED.
    ApplicationStatusResponse updateStatus(Integer applicationId, String targetStatus, String reason);

    // Xem lịch sử đổi trạng thái của một đơn ứng tuyển, mới nhất trước.
    List<ApplicationStatusHistoryResponse> getStatusHistory(Integer applicationId);
}