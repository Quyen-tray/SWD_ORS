package org.ors.subsystem.recruiter.candidate_management.controller;

import org.ors.subsystem.recruiter.candidate_management.service.IRecruiterCandidateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-22..UC-32 - Xem, loc, danh gia ung vien ung tuyen; len lich phong van.
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
@RequestMapping("/recruiter/candidates")
public class RecruiterCandidateController {

    private final IRecruiterCandidateService recruiterCandidateService;

    public RecruiterCandidateController(IRecruiterCandidateService recruiterCandidateService) {
        this.recruiterCandidateService = recruiterCandidateService;
    }

    // TODO: them cac endpoint cua UC-22..UC-32.
}
