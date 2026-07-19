package org.ors.subsystem.moderation.job_moderation.service;

import org.ors.subsystem.moderation.job_moderation.dto.AuditLogResponse;

import java.time.Instant;
import java.util.List;

// UC-50.
public interface IAuditLogQueryService {
    List<AuditLogResponse> query(Integer moderatorId, String entityType, Integer entityId,
                                  String actionType, Instant from, Instant to,
                                  boolean mineOnly, Integer requestingModeratorId);
}
