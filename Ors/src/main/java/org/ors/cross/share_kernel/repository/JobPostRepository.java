package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Bảng job_posts thuộc về feature Job Posting của thành viên khác. Subsystem
// moderator_admin chỉ đọc nó cho đúng một câu hỏi: danh mục sắp xoá có còn tin tuyển
// dụng nào đang hoạt động không (BR-14).
@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Integer> {

    boolean existsByCategoryIdAndStatus(Integer categoryId, String status);

    // UC-49 - đếm tin đang chờ duyệt cho panel Dashboard (Facade).
    long countByValidationStatus(String validationStatus);

    @org.springframework.data.jpa.repository.Query("SELECT jp FROM JobPost jp WHERE " +
            "(jp.status = 'ACTIVE' OR jp.status = 'PUBLISHED') AND " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(jp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(CAST(jp.description AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:categoryName IS NULL OR :categoryName = '' OR LOWER(jp.category.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%')))")
    org.springframework.data.domain.Page<JobPost> searchJobs(
            @org.springframework.data.repository.query.Param("keyword") String keyword,
            @org.springframework.data.repository.query.Param("categoryName") String categoryName,
            org.springframework.data.domain.Pageable pageable);
}
