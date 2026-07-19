package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.JobReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Dung boi: Moderation (2.3).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface JobReportRepository extends JpaRepository<JobReport, Integer> {

    // UC-45 (lọc theo trạng thái) + UC-49 (đếm report đang mở / tính SLA%).
    List<JobReport> findByStatusIn(List<String> statuses);
}
