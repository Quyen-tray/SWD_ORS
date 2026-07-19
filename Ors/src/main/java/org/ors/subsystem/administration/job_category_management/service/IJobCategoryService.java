package org.ors.subsystem.administration.job_category_management.service;

import org.ors.subsystem.administration.job_category_management.dto.JobCategoryRequest;
import org.ors.subsystem.administration.job_category_management.dto.JobCategoryResponse;

import java.util.List;

public interface IJobCategoryService {

    // UC-58
    List<JobCategoryResponse> getAllJobCategories();

    // UC-57
    JobCategoryResponse createJobCategory(JobCategoryRequest request);

    // UC-59
    JobCategoryResponse updateJobCategory(Integer id, JobCategoryRequest request);

    // UC-60
    void deleteJobCategory(Integer id);
}
