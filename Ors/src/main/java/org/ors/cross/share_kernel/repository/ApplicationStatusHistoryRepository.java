package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// Dung boi: Recruiter (2.1) + Candidate (2.2).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, Integer> {

    // UC-04 (GET /applications/{id}/status-history) - lịch sử đổi trạng thái của một
    // đơn ứng tuyển, mới nhất trước. LEFT JOIN FETCH changedBy vì cột này nullable
    // (changed_by không NOT NULL trong db.sql) - JOIN FETCH thường sẽ loại bỏ dòng có
    // changedBy null, LEFT JOIN FETCH thì không.
    @Query("SELECT h FROM ApplicationStatusHistory h "
            + "LEFT JOIN FETCH h.changedBy "
            + "WHERE h.application.id = :applicationId "
            + "ORDER BY h.changedAt DESC")
    List<ApplicationStatusHistory> findByApplicationIdOrderByChangedAtDesc(Integer applicationId);
}