package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Dung boi: Candidate (2.2).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Integer> {

    // UC-72: Tìm cài đặt thông báo theo candidate_id.
    Optional<NotificationSetting> findByCandidate_Id(Integer candidateId);
}
