package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Dung boi: Recruiter (2.1) + Candidate (2.2).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {

    // UC-71: Lấy danh sách đơn ứng tuyển của candidate.
    List<JobApplication> findByCandidate_Id(Integer candidateId);

    // UC-70 (BR-03): Kiểm tra đã ứng tuyển vị trí này chưa (Single Application
    // Limit).
    Optional<JobApplication> findByCandidate_IdAndJobPost_Id(Integer candidateId, Integer jobPostId);

    // UC-73: Đếm tổng số đơn đã nộp cho dashboard.
    long countByCandidate_Id(Integer candidateId);

    // UC-01 (candidate_management, nhánh Strategy KeywordSearchStrategy) - Recruiter gõ
    // từ khoá, khớp trên tên hoặc email ứng viên, chỉ trong công ty của Recruiter đang
    // đăng nhập. JOIN FETCH để CandidateSummaryResponse đọc candidate/jobPost/cv được
    // ngay, không cần mở lại transaction ở tầng service.
    @Query("SELECT ja FROM JobApplication ja "
            + "JOIN FETCH ja.candidate c "
            + "JOIN FETCH ja.jobPost jp "
            + "JOIN FETCH ja.cv "
            + "WHERE jp.company.id = :companyId "
            + "AND (LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "     OR LOWER(c.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<JobApplication> findByCompanyAndKeyword(Integer companyId, String keyword);

    // UC-01 (nhánh JobStatusFilterStrategy) - lọc theo tin tuyển dụng và/hoặc trạng thái.
    // Truyền null cho tiêu chí không lọc.
    @Query("SELECT ja FROM JobApplication ja "
            + "JOIN FETCH ja.candidate "
            + "JOIN FETCH ja.jobPost jp "
            + "JOIN FETCH ja.cv "
            + "WHERE jp.company.id = :companyId "
            + "AND (:jobPostId IS NULL OR jp.id = :jobPostId) "
            + "AND (:status IS NULL OR ja.status = :status)")
    List<JobApplication> findByCompanyAndFilter(Integer companyId, Integer jobPostId, String status);

    // UC-01 (nhánh ListAllStrategy) - chưa nhập từ khoá lẫn bộ lọc thì lấy hết đơn ứng
    // tuyển của công ty.
    @Query("SELECT ja FROM JobApplication ja "
            + "JOIN FETCH ja.candidate "
            + "JOIN FETCH ja.jobPost jp "
            + "JOIN FETCH ja.cv "
            + "WHERE jp.company.id = :companyId")
    List<JobApplication> findByCompany(Integer companyId);

    // UC-04/UC-07 (candidate_management.state) - đổi trạng thái pipeline. JOIN FETCH
    // jobPost + company để ApplicationStatusService kiểm tra đơn này có thuộc công ty
    // của Recruiter đang đăng nhập không, mà không cần mở lại transaction.
    @Query("SELECT ja FROM JobApplication ja "
            + "JOIN FETCH ja.jobPost jp "
            + "JOIN FETCH jp.company "
            + "WHERE ja.id = :applicationId")
    Optional<JobApplication> findWithJobPostAndCompanyById(Integer applicationId);

    // UC-7x (cv_management, phía Candidate) - kiểm tra CV có đang được dùng trong đơn
    // ứng tuyển nào không, chặn xoá CV nếu true (giữ toàn vẹn lịch sử ứng tuyển).
    boolean existsByCv_Id(Integer cvId);
}