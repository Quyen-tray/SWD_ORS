package org.ors.subsystem.candidate.application_tracking.controller;

import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.entity.NotificationSetting;
import org.ors.subsystem.candidate.application_tracking.dto.DashboardStatsResponse;
import org.ors.subsystem.candidate.application_tracking.service.IApplicationTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// UC-70, UC-71, UC-72, UC-73
@RestController
@RequestMapping("/candidate/applications")
public class ApplicationTrackingController {

    private final IApplicationTrackingService trackingService;

    public ApplicationTrackingController(IApplicationTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    // UC-70: Nộp đơn
    @PostMapping("/{candidateId}/apply")
    public ResponseEntity<JobApplication> applyForJob(
            @PathVariable Integer candidateId,
            @RequestParam Integer jobPostId,
            @RequestParam Integer cvId) {
        return ResponseEntity.ok(trackingService.applyForJob(candidateId, jobPostId, cvId));
    }

    // UC-71: Danh sách đơn
    @GetMapping("/{candidateId}")
    public ResponseEntity<List<JobApplication>> getApplicationsByCandidate(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(trackingService.getApplicationsByCandidate(candidateId));
    }

    // UC-71: Rút đơn
    @PatchMapping("/{applicationId}/withdraw")
    public ResponseEntity<JobApplication> withdrawApplication(@PathVariable Integer applicationId) {
        return ResponseEntity.ok(trackingService.withdrawApplication(applicationId));
    }

    // UC-73: Dashboard stats
    @GetMapping("/{candidateId}/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(trackingService.getDashboardStats(candidateId));
    }

    // UC-72: Notifications
    @PutMapping("/{candidateId}/notifications")
    public ResponseEntity<NotificationSetting> updateNotificationSettings(
            @PathVariable Integer candidateId,
            @RequestBody NotificationSetting settings) {
        return ResponseEntity.ok(trackingService.updateNotificationSettings(candidateId, settings));
    }
}
