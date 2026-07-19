package org.ors.subsystem.moderation.job_moderation.controller;

import org.ors.subsystem.moderation.job_moderation.dto.DashboardSummaryResponse;
import org.ors.subsystem.moderation.job_moderation.security.CurrentModeratorResolver;
import org.ors.subsystem.moderation.job_moderation.service.IModerationDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UC-49.
@RestController
@RequestMapping("/moderation/dashboard")
public class ModerationDashboardController {

    private final IModerationDashboardService dashboardService;
    private final CurrentModeratorResolver currentModeratorResolver;

    public ModerationDashboardController(IModerationDashboardService dashboardService,
                                          CurrentModeratorResolver currentModeratorResolver) {
        this.dashboardService = dashboardService;
        this.currentModeratorResolver = currentModeratorResolver;
    }

    @GetMapping
    public DashboardSummaryResponse getSummary() {
        return dashboardService.getSummary(currentModeratorResolver.resolveModeratorId());
    }
}
