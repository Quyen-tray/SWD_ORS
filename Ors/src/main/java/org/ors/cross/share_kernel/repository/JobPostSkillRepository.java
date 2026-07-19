package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.JobPostSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Dung boi: Recruiter (2.1).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface JobPostSkillRepository extends JpaRepository<JobPostSkill, Integer> {
}
