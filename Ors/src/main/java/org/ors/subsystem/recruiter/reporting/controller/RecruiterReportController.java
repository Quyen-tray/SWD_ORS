package org.ors.subsystem.recruiter.reporting.controller;

import org.ors.subsystem.recruiter.reporting.service.IRecruiterReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-35 - Thong ke va xuat bao cao tuyen dung.
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
@RequestMapping("/recruiter/reports")
public class RecruiterReportController {

    private final IRecruiterReportService recruiterReportService;

    public RecruiterReportController(IRecruiterReportService recruiterReportService) {
        this.recruiterReportService = recruiterReportService;
    }

    // TODO: them cac endpoint cua UC-35.
}
