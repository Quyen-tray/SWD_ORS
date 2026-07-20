package org.ors.subsystem.recruiter.candidate_management.lookup;

import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.repository.JobApplicationRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

// Nhánh 2 của UC-01: Recruiter không gõ từ khoá mà chọn tin tuyển dụng và/hoặc trạng
// thái. Chỉ nhận việc khi không có từ khoá, nên @Order sau KeywordSearchStrategy.
@Order(2)
@Component
public class JobStatusFilterStrategy implements CandidateLookupStrategy {

    private final JobApplicationRepository jobApplicationRepository;

    public JobStatusFilterStrategy(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public boolean supports(CandidateCriteria criteria) {
        return !criteria.hasKeyword() && criteria.hasJobPostOrStatus();
    }

    @Override
    public List<JobApplication> lookup(CandidateCriteria criteria) {
        // Truyền null cho tiêu chí không chọn: câu query bỏ qua vế đó.
        return jobApplicationRepository.findByCompanyAndFilter(
                criteria.companyId(), criteria.jobPostId(), criteria.status());
    }
}