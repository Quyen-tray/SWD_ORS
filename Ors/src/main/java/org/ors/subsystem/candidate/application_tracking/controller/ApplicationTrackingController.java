package org.ors.subsystem.candidate.application_tracking.controller;

import org.ors.subsystem.candidate.application_tracking.service.IApplicationTrackingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-71, UC-73, UC-74 - Theo doi don ung tuyen, thong bao, dashboard ung vien.
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
@RequestMapping("/candidate/applications")
public class ApplicationTrackingController {

    private final IApplicationTrackingService applicationTrackingService;

    public ApplicationTrackingController(IApplicationTrackingService applicationTrackingService) {
        this.applicationTrackingService = applicationTrackingService;
    }

    // TODO: them cac endpoint cua UC-71, UC-73, UC-74.
}
