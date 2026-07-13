package org.ors.subsystem.candidate.cv_management.controller;

import org.ors.subsystem.candidate.cv_management.service.ICvService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-66, UC-67 - Ho so ca nhan va quan ly CV.
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
@RequestMapping("/candidate/cvs")
public class CvController {

    private final ICvService cvService;

    public CvController(ICvService cvService) {
        this.cvService = cvService;
    }

    // TODO: them cac endpoint cua UC-66, UC-67.
}
