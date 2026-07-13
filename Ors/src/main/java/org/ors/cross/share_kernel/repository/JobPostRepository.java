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
}
