package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Dung boi: Candidate (2.2).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Integer> {

    // UC-66: Tìm profile theo user_id để hiển thị và cập nhật hồ sơ cá nhân.
    Optional<CandidateProfile> findByUser_Id(Integer userId);
}
