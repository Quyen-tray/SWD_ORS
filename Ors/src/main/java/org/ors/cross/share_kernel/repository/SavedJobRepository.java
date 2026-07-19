package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.SavedJob;
import org.ors.cross.share_kernel.entity.SavedJobId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Dung boi: Candidate (2.2).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, SavedJobId> {

    // UC-69: Lấy danh sách việc đã lưu.
    List<SavedJob> findByCandidate_Id(Integer candidateId);

    // UC-69: Tìm bookmark cụ thể để toggle save/unsave.
    Optional<SavedJob> findById_CandidateIdAndId_JobPostId(Integer candidateId, Integer jobPostId);

    // Đếm số lượng tin đã lưu cho dashboard.
    long countById_CandidateId(Integer candidateId);
}
