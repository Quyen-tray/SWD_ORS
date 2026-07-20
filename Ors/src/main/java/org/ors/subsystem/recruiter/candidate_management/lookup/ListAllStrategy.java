package org.ors.subsystem.recruiter.candidate_management.lookup;

import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.repository.JobApplicationRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

// Nhánh 3 của UC-01: Recruiter mở màn hình mà chưa nhập tiêu chí nào.
// Đây là chiến lược mặc định: nhận mọi tiêu chí, nên @Order cao nhất để được hỏi sau
// cùng. Nhờ có nó mà CandidateLookupSelector luôn tìm được một chiến lược, không bao
// giờ rơi vào trường hợp "không ai nhận việc".
//
// Đặt tên bean riêng ("candidateListAllStrategy") - cùng lý do với KeywordSearchStrategy
// trong package này: administration.user_management.lookup cũng có 1 class tên
// "ListAllStrategy", để @Component tự suy tên mặc định sẽ trùng bean, Spring báo
// ConflictingBeanDefinitionException lúc khởi động.
@Order(Integer.MAX_VALUE)
@Component("candidateListAllStrategy")
public class ListAllStrategy implements CandidateLookupStrategy {

    private final JobApplicationRepository jobApplicationRepository;

    public ListAllStrategy(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public boolean supports(CandidateCriteria criteria) {
        return true;
    }

    @Override
    public List<JobApplication> lookup(CandidateCriteria criteria) {
        return jobApplicationRepository.findByCompany(criteria.companyId());
    }
}