package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Dung boi: Recruiter (2.1) + Candidate (2.2).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {

    // UC-71: Lấy danh sách đơn ứng tuyển của candidate.
    List<JobApplication> findByCandidate_Id(Integer candidateId);

    // UC-70 (BR-03): Kiểm tra đã ứng tuyển vị trí này chưa (Single Application Limit).
    Optional<JobApplication> findByCandidate_IdAndJobPost_Id(Integer candidateId, Integer jobPostId);

    // UC-73: Đếm tổng số đơn đã nộp cho dashboard.
    long countByCandidate_Id(Integer candidateId);
}
