package org.ors.subsystem.administration.audit.controller;

import org.ors.subsystem.administration.audit.AuditLogService;
import org.ors.subsystem.administration.audit.dto.AuditLogResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// UC-61.
@RestController("administrationAuditLogController")
@RequestMapping("/admin/audit-log-management")
public class AdminAuditLogController {

    private final AuditLogService auditLogService;

    public AdminAuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    // UC-61
    @GetMapping
    public List<AuditLogResponse> getAuditLogs() {
        return auditLogService.getAllLogs();
    }
}
