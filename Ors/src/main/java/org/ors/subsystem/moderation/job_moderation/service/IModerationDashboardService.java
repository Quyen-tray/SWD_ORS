package org.ors.subsystem.moderation.job_moderation.service;

import org.ors.subsystem.moderation.job_moderation.dto.DashboardSummaryResponse;

// UC-49.
public interface IModerationDashboardService {
    DashboardSummaryResponse getSummary(Integer moderatorId);
}
