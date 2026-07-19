package org.ors.subsystem.candidate.job_search.service;

import org.ors.cross.share_kernel.entity.SavedJob;

import java.util.List;

// UC-68, UC-69, UC-70 - Tim kiem tin tuyen dung, luu tin, ung tuyen.
//
// Controller phu thuoc vao interface nay chu khong phu thuoc vao lop hien thuc,
// nen co the thay hoac mock phan hien thuc ma khong dung toi controller.
public interface IJobSearchService {

    // UC-69: Lấy danh sách việc đã lưu (Saved Jobs).
    List<SavedJob> getSavedJobs(Integer candidateId);

    // UC-69: Toggle save/unsave một job posting (bookmark).
    String toggleSavedJob(Integer candidateId, Integer jobPostId);

    // TODO: UC-68 - searchJobs(keyword, filters), getJobDetail(jobPostId)
    // TODO: UC-70 - applyForJob(candidateId, jobPostId, cvId) — đã đặt ở application_tracking
}
