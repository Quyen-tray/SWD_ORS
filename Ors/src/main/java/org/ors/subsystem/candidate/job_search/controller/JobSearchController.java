package org.ors.subsystem.candidate.job_search.controller;

import org.ors.cross.share_kernel.entity.SavedJob;
import org.ors.subsystem.candidate.job_search.service.IJobSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// UC-68, UC-69, UC-70 - Tim kiem tin tuyen dung, luu tin, ung tuyen.
//
// Quy uoc:
//   - Path o day KHONG co /api/v1: context-path da khai bao trong application.properties.
//   - Path phai khop endpoints.js ben frontend (shared/api/endpoints.js).
//   - Controller chi anh xa HTTP sang service, KHONG chua business rule.
@RestController
@RequestMapping("/candidate/jobs")
public class JobSearchController {

    private final IJobSearchService jobSearchService;

    public JobSearchController(IJobSearchService jobSearchService) {
        this.jobSearchService = jobSearchService;
    }

    // UC-69 Scenario B: Lấy danh sách việc đã lưu (Saved Jobs).
    @GetMapping("/saved/{candidateId}")
    public ResponseEntity<List<SavedJob>> getSavedJobs(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(jobSearchService.getSavedJobs(candidateId));
    }

    // UC-69 Scenario A: Toggle save/unsave một job posting.
    @PostMapping("/saved/{candidateId}/toggle")
    public ResponseEntity<String> toggleSavedJob(
            @PathVariable Integer candidateId,
            @RequestParam Integer jobPostId) {
        return ResponseEntity.ok(jobSearchService.toggleSavedJob(candidateId, jobPostId));
    }

    // UC-68: Tìm kiếm việc làm.
    @GetMapping("/search")
    public ResponseEntity<List<org.ors.cross.share_kernel.entity.JobPost>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(jobSearchService.searchJobs(keyword, category, location, type, minSalary, sortBy, page));
    }

    // UC-68: Xem chi tiết việc làm.
    @GetMapping("/{jobPostId}")
    public ResponseEntity<org.ors.cross.share_kernel.entity.JobPost> getJobDetail(@PathVariable Integer jobPostId) {
        return ResponseEntity.ok(jobSearchService.getJobDetail(jobPostId));
    }
}
