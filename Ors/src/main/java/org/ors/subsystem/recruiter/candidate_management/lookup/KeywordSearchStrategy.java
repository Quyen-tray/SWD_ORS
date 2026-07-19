package org.ors.subsystem.recruiter.candidate_management.lookup;

import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.repository.JobApplicationRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

// Nhánh 1 của UC-01: Recruiter gõ từ khoá vào ô tìm kiếm (tên hoặc email ứng viên).
// Từ khoá được ưu tiên hơn bộ lọc, nên @Order thấp nhất (được hỏi trước).
@Order(1)
@Component
public class KeywordSearchStrategy implements CandidateLookupStrategy {

    private final JobApplicationRepository jobApplicationRepository;

    public KeywordSearchStrategy(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public boolean supports(CandidateCriteria criteria) {
        return criteria.hasKeyword();
    }

    @Override
    public List<JobApplication> lookup(CandidateCriteria criteria) {
        return jobApplicationRepository.findByCompanyAndKeyword(criteria.companyId(), criteria.keyword());
    }
}