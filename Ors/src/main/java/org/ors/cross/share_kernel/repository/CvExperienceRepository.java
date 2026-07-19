package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.CvExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Dung boi: Candidate (2.2).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface CvExperienceRepository extends JpaRepository<CvExperience, Integer> {
    void deleteByCv_Id(Integer cvId);
    java.util.List<CvExperience> findByCv_Id(Integer cvId);
}
