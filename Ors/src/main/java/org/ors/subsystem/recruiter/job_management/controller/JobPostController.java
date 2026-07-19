package org.ors.subsystem.recruiter.job_management.controller;

import org.ors.subsystem.recruiter.job_management.service.IJobPostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-01..UC-21 - Dang ky recruiter, ho so cong ty, tao va dang tin tuyen dung.
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
@RequestMapping("/recruiter/job-posts")
public class JobPostController {

    private final IJobPostService jobPostService;

    public JobPostController(IJobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    // TODO: them cac endpoint cua UC-01..UC-21.
}
