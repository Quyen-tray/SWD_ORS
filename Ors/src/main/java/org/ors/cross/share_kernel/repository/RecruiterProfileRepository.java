package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Dung boi: Recruiter (2.1) + Moderation (2.3).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {

    // UC-47 - lấy email Recruiter của công ty đăng tin để báo khi report được resolve.
    @Query("SELECT rp.user.email FROM RecruiterProfile rp WHERE rp.company.id = :companyId")
    List<String> findRecruiterEmailsByCompanyId(Integer companyId);

    // UC-01 (candidate_management) - từ User đang đăng nhập suy ra Recruiter đang thao
    // tác thuộc công ty nào, để mọi tra cứu ứng viên chỉ giới hạn trong công ty đó.
    Optional<RecruiterProfile> findByUser_Id(Integer userId);
}