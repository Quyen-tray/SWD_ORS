package org.ors.subsystem.moderation.job_moderation.controller;

import org.ors.subsystem.moderation.job_moderation.service.IJobModerationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-40..UC-52 - Duyet tin tuyen dung, xu ly bao cao vi pham, nhat ky kiem duyet.
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
@RequestMapping("/moderation/job-posts")
public class JobModerationController {

    private final IJobModerationService jobModerationService;

    public JobModerationController(IJobModerationService jobModerationService) {
        this.jobModerationService = jobModerationService;
    }

    // TODO: them cac endpoint cua UC-40..UC-52.
}
