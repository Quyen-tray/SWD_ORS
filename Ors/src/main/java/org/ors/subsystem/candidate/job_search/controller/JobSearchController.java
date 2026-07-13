package org.ors.subsystem.candidate.job_search.controller;

import org.ors.subsystem.candidate.job_search.service.IJobSearchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-68, UC-69, UC-70 - Tim kiem tin tuyen dung, luu tin, ung tuyen.
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
@RequestMapping("/candidate/jobs")
public class JobSearchController {

    private final IJobSearchService jobSearchService;

    public JobSearchController(IJobSearchService jobSearchService) {
        this.jobSearchService = jobSearchService;
    }

    // TODO: them cac endpoint cua UC-68, UC-69, UC-70.
}
