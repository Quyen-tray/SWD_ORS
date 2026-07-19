package org.ors.subsystem.moderation.job_moderation.controller;

import org.ors.subsystem.moderation.job_moderation.dto.AuditLogResponse;
import org.ors.subsystem.moderation.job_moderation.security.CurrentModeratorResolver;
import org.ors.subsystem.moderation.job_moderation.service.IAuditLogQueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

// UC-50 - đọc, không có ghi. Audit entry là bất biến (immutable), controller này không có
// endpoint POST/PUT nào.
@RestController
@RequestMapping("/moderation/audit-logs")
public class AuditLogController {

    private final IAuditLogQueryService auditLogQueryService;
    private final CurrentModeratorResolver currentModeratorResolver;

    public AuditLogController(IAuditLogQueryService auditLogQueryService,
                               CurrentModeratorResolver currentModeratorResolver) {
        this.auditLogQueryService = auditLogQueryService;
        this.currentModeratorResolver = currentModeratorResolver;
    }

    @GetMapping
    public List<AuditLogResponse> query(
            @RequestParam(required = false) Integer moderatorId,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) Integer entityId,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(defaultValue = "false") boolean mineOnly) {
        return auditLogQueryService.query(moderatorId, entityType, entityId, actionType, from, to,
                mineOnly, currentModeratorResolver.resolveModeratorId());
    }
}
