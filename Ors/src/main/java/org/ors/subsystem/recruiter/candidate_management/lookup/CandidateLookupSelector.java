package org.ors.subsystem.recruiter.candidate_management.lookup;

import org.ors.cross.share_kernel.entity.JobApplication;
import org.springframework.stereotype.Component;

import java.util.List;

// Chọn chiến lược tra cứu ứng với tiêu chí Recruiter nhập vào.
//
// Spring tự tiêm CẢ DANH SÁCH các bean cài đặt CandidateLookupStrategy vào constructor,
// và xếp sẵn theo @Order. Vì vậy thêm một cách tra cứu mới (vd lọc theo khoảng ngày nộp
// đơn) chỉ cần viết thêm một class @Component implements CandidateLookupStrategy - KHÔNG
// phải sửa file này, cũng không phải sửa RecruiterCandidateService.
//
// ListAllStrategy nhận mọi tiêu chí và đứng cuối danh sách, nên luôn có người nhận việc.
@Component
public class CandidateLookupSelector {

    private final List<CandidateLookupStrategy> strategies;

    public CandidateLookupSelector(List<CandidateLookupStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<JobApplication> lookup(CandidateCriteria criteria) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(criteria))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Không có chiến lược tra cứu nào nhận tiêu chí này"))
                .lookup(criteria);
    }
}