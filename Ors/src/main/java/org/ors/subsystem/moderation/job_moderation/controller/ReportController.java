package org.ors.subsystem.moderation.job_moderation.controller;

import org.ors.subsystem.moderation.job_moderation.dto.CloseReportRequest;
import org.ors.subsystem.moderation.job_moderation.dto.ReportDetailResponse;
import org.ors.subsystem.moderation.job_moderation.dto.ReportSummaryResponse;
import org.ors.subsystem.moderation.job_moderation.dto.ResolveReportRequest;
import org.ors.subsystem.moderation.job_moderation.security.CurrentModeratorResolver;
import org.ors.subsystem.moderation.job_moderation.service.IReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// UC-45..48 - Report queue, investigation, resolution, closure. Controller chỉ ánh xạ HTTP
// sang lời gọi service, không chứa business rule (xem ReportService + package state).
//
// Tách riêng khỏi JobModerationController (stub UC-40..44, ngoài phạm vi demo) để luồng
// UC-45..50 gọn và dễ review độc lập.
@RestController
@RequestMapping("/moderation/reports")
public class ReportController {

    private final IReportService reportService;
    private final CurrentModeratorResolver currentModeratorResolver;

    public ReportController(IReportService reportService, CurrentModeratorResolver currentModeratorResolver) {
        this.reportService = reportService;
        this.currentModeratorResolver = currentModeratorResolver;
    }

    // UC-45
    @GetMapping
    public List<ReportSummaryResponse> getQueue(@RequestParam(required = false) String status,
                                                 @RequestParam(required = false) String sort) {
        return reportService.getQueue(status, sort, currentModeratorResolver.resolveModeratorId());
    }

    // UC-46
    @GetMapping("/{id}")
    public ReportDetailResponse getDetail(@PathVariable Integer id) {
        return reportService.getDetail(id);
    }

    // UC-46
    @PutMapping("/{id}/investigate")
    public ResponseEntity<Void> investigate(@PathVariable Integer id) {
        reportService.investigate(id, currentModeratorResolver.resolveModeratorId());
        return ResponseEntity.noContent().build();
    }

    // UC-47
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable Integer id, @RequestBody ResolveReportRequest request) {
        reportService.resolve(id, request, currentModeratorResolver.resolveModeratorId());
        return ResponseEntity.noContent().build();
    }

    // UC-48
    @PutMapping("/{id}/close")
    public ResponseEntity<Void> close(@PathVariable Integer id, @RequestBody CloseReportRequest request) {
        reportService.close(id, request, currentModeratorResolver.resolveModeratorId());
        return ResponseEntity.noContent().build();
    }
}
