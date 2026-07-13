package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.JobReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Dung boi: Moderation (2.3).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface JobReportRepository extends JpaRepository<JobReport, Integer> {
}
