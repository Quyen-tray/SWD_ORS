package org.ors.subsystem.administration.job_category_management.controller;

import org.ors.subsystem.administration.job_category_management.dto.JobCategoryRequest;
import org.ors.subsystem.administration.job_category_management.dto.JobCategoryResponse;
import org.ors.subsystem.administration.job_category_management.service.IJobCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// UC-57..60.
@RestController
@RequestMapping("/admin/job-category-management")
public class JobCategoryController {

    private final IJobCategoryService jobCategoryService;

    public JobCategoryController(IJobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    // UC-58
    @GetMapping
    public List<JobCategoryResponse> getJobCategories() {
        return jobCategoryService.getAllJobCategories();
    }

    // UC-57
    @PostMapping
    public ResponseEntity<JobCategoryResponse> createJobCategory(@RequestBody JobCategoryRequest request) {
        return ResponseEntity.ok(jobCategoryService.createJobCategory(request));
    }

    // UC-59
    @PutMapping("/{id}")
    public ResponseEntity<JobCategoryResponse> updateJobCategory(@PathVariable Integer id,
                                                                 @RequestBody JobCategoryRequest request) {
        return ResponseEntity.ok(jobCategoryService.updateJobCategory(id, request));
    }

    // UC-60
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobCategory(@PathVariable Integer id) {
        jobCategoryService.deleteJobCategory(id);
        return ResponseEntity.noContent().build();
    }
}
