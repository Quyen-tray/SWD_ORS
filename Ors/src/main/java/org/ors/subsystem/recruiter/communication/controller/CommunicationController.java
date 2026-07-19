package org.ors.subsystem.recruiter.communication.controller;

import org.ors.subsystem.recruiter.communication.service.ICommunicationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-33 - Lich su trao doi giua recruiter va ung vien.
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
@RequestMapping("/recruiter/communications")
public class CommunicationController {

    private final ICommunicationService communicationService;

    public CommunicationController(ICommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    // TODO: them cac endpoint cua UC-33.
}
