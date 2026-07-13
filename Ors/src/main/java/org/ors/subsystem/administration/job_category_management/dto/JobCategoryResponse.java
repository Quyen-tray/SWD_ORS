package org.ors.subsystem.administration.job_category_management.dto;

import org.ors.cross.share_kernel.entity.JobCategory;

public record JobCategoryResponse(Integer id, String categoryName) {

    public static JobCategoryResponse from(JobCategory category) {
        return new JobCategoryResponse(category.getId(), category.getCategoryName());
    }
}
