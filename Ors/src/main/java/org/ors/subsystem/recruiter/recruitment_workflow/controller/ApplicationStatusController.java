package org.ors.subsystem.recruiter.recruitment_workflow.controller;

import org.ors.subsystem.recruiter.recruitment_workflow.dto.ApplicationStatusHistoryResponse;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.ApplicationStatusResponse;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.UpdateStatusRequest;
import org.ors.subsystem.recruiter.recruitment_workflow.service.IApplicationStatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// UC-04 Update Pipeline Status (recruitment_workflow), tái dùng ở UC-07. Xem
// 00_KE_HOACH_TONG_QUAN.md để biết phạm vi hiện tại.
//
// Quy uoc:
//   - Path o day KHONG co /api/v1: context-path da khai bao trong application.properties.
//   - Path va HTTP method phai khop endpoints.js + pipelineApi.js ben frontend
//     (ENDPOINTS.applications.updateStatus/history dung PATCH, khong phai PUT).
//   - Controller chi anh xa HTTP sang service, KHONG chua business rule.
@RestController
@RequestMapping("/applications")
public class ApplicationStatusController {

    private final IApplicationStatusService applicationStatusService;

    public ApplicationStatusController(IApplicationStatusService applicationStatusService) {
        this.applicationStatusService = applicationStatusService;
    }

    // UC-04 luồng chính (đi tiếp 1 bước) + A1 (Reject, reason bắt buộc). UC-07
    // Hire/Offer dùng lại đúng endpoint này (Offered -> Hired cũng là một bước advance()).
    @PatchMapping("/{id}/status")
    public ApplicationStatusResponse updateStatus(@PathVariable Integer id,
                                                   @RequestBody UpdateStatusRequest request) {
        return applicationStatusService.updateStatus(id, request.status(), request.reason());
    }

    // Lịch sử đổi trạng thái của một đơn ứng tuyển, mới nhất trước.
    @GetMapping("/{id}/status-history")
    public List<ApplicationStatusHistoryResponse> getStatusHistory(@PathVariable Integer id) {
        return applicationStatusService.getStatusHistory(id);
    }
}