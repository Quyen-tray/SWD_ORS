package org.ors.subsystem.moderation.company_verification.controller;

import org.ors.subsystem.moderation.company_verification.service.ICompanyVerificationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-36..UC-39 - Duyet, tu choi, dinh chi ho so cong ty.
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
@RequestMapping("/moderation/company-verifications")
public class CompanyVerificationController {

    private final ICompanyVerificationService companyVerificationService;

    public CompanyVerificationController(ICompanyVerificationService companyVerificationService) {
        this.companyVerificationService = companyVerificationService;
    }

    // TODO: them cac endpoint cua UC-36..UC-39.
}
