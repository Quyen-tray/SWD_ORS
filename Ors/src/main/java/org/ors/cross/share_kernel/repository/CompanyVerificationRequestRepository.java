package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.CompanyVerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Dung boi: Recruiter (2.1) + Moderation (2.3).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface CompanyVerificationRequestRepository extends JpaRepository<CompanyVerificationRequest, Integer> {
}
