package org.ors.subsystem.candidate.job_search.service;

import org.ors.cross.share_kernel.entity.JobPost;
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

    // UC-68: Tìm kiếm việc làm.
    List<JobPost> searchJobs(String keyword, String category, String location, String type, Double minSalary, String sortBy, int page);

    // UC-68: Xem chi tiết việc làm.
    JobPost getJobDetail(Integer jobPostId);
}
