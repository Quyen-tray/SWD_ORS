package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Dung boi: Recruiter (2.1).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    // UC-05 (recruitment_workflow) - tính số vòng phỏng vấn kế tiếp của một đơn ứng
    // tuyển: round mới = số interview đã có (kể cả đã huỷ) + 1. Không đếm lại từ 1 khi
    // có lịch bị huỷ để tránh 2 interview trùng round cho cùng 1 application.
    long countByApplication_Id(Integer applicationId);

    // UC-05 - lấy 1 interview kèm company (qua application -> jobPost -> company) để
    // InterviewService kiểm tra interview này có thuộc công ty của Recruiter đang đăng
    // nhập không, dùng chung cho cancel/reschedule/xem chi tiết - JOIN FETCH để không
    // cần mở lại transaction ở tầng service (cùng kiểu với
    // JobApplicationRepository.findWithJobPostAndCompanyById của UC-04).
    @Query("SELECT i FROM Interview i "
            + "JOIN FETCH i.application a "
            + "JOIN FETCH a.jobPost jp "
            + "JOIN FETCH jp.company "
            + "WHERE i.id = :interviewId")
    Optional<Interview> findWithApplicationAndCompanyById(Integer interviewId);
}