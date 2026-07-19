package org.ors.subsystem.recruiter.dashboard.controller;

import org.ors.subsystem.recruiter.dashboard.service.IRecruiterDashboardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-34 - Tong quan tuyen dung cua recruiter.
//
// KHUNG (base). Nguoi phu trach phan nay dien endpoint vao day.
// Mau da hoan chinh de tham chieu: subsystem/administration/user_management
// (controller + service + interface + dto + state pattern + audit).
//
// Quy uoc:
//   - Path o day KHONG co /api/v1: context-path da khai bao trong application.properties.
//   - Path phai khop endpoints.js ben frontend (shared/api/endpoints.js).
//   - Controller chi anh xa HTTP sang service, KHONG chua business rule.
@RestController
@RequestMapping("/recruiter/dashboard")
public class RecruiterDashboardController {

    private final IRecruiterDashboardService recruiterDashboardService;

    public RecruiterDashboardController(IRecruiterDashboardService recruiterDashboardService) {
        this.recruiterDashboardService = recruiterDashboardService;
    }

    // TODO: them cac endpoint cua UC-34.
}
