package org.ors.subsystem.recruiter.candidate_management.service;

import org.ors.cross.Iam.security.user.CustomUserDetails;
import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.entity.RecruiterProfile;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.RecruiterProfileRepository;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.ors.subsystem.recruiter.candidate_management.dto.CandidateListResponse;
import org.ors.subsystem.recruiter.candidate_management.dto.CandidateSummaryResponse;
import org.ors.subsystem.recruiter.candidate_management.lookup.CandidateCriteria;
import org.ors.subsystem.recruiter.candidate_management.lookup.CandidateLookupSelector;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

// UC-01 (candidate_management). Nơi duy nhất chứa business rule của phần này -
// controller không biết luật, repository không biết luật.
@Service
public class RecruiterCandidateService implements IRecruiterCandidateService {

    // Màn hình đăng nhập chưa được cài đặt thật (org.ors.cross.Iam.auth vẫn rỗng, giống
    // ghi chú ở UserService), nên khi chưa có token thì tạm lấy Recruiter đã seed sẵn này.
    // Khi luồng đăng nhập xong thì nhánh fallback này bỏ đi, phần còn lại giữ nguyên.
    private static final String RECRUITER_EMAIL_FALLBACK = "hong.le@fpt.com.vn";

    private final CandidateLookupSelector candidateLookupSelector;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public RecruiterCandidateService(CandidateLookupSelector candidateLookupSelector,
                                     RecruiterProfileRepository recruiterProfileRepository,
                                     UserRepository userRepository) {
        this.candidateLookupSelector = candidateLookupSelector;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateListResponse getCandidates(String keyword, String jobPostId, String status,
                                               int page, int pageSize) {
        // Strategy pattern: 3 cách tra cứu của UC-01 (từ khoá / lọc / lấy tất cả), mỗi
        // cách là một class riêng trong package .lookup, không còn nằm chung trong một
        // chuỗi if/else ở đây. Service chỉ nói "tra cứu theo tiêu chí này, trong công ty
        // của Recruiter đang đăng nhập", không quyết định tra cứu bằng cách nào.
        Integer companyId = currentRecruiterCompanyId();
        CandidateCriteria criteria = CandidateCriteria.of(companyId, keyword, jobPostId, status);
        List<JobApplication> applications = candidateLookupSelector.lookup(criteria);

        return paginate(applications, page, pageSize);
    }

    // Sắp đơn mới nộp lên trước rồi cắt trang trong bộ nhớ. Quy mô dữ liệu của một công
    // ty (vài trăm đơn ứng tuyển) đủ nhỏ để làm vậy mà không cần Pageable ở tầng
    // repository - giữ 3 chiến lược tra cứu đơn giản, chỉ trả về danh sách.
    private CandidateListResponse paginate(List<JobApplication> applications, int page, int pageSize) {
        List<JobApplication> sorted = applications.stream()
                .sorted(Comparator.comparing(JobApplication::getAppliedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        int safePage = Math.max(page, 1);
        int safeSize = pageSize <= 0 ? 10 : pageSize;
        int fromIndex = Math.min((safePage - 1) * safeSize, sorted.size());
        int toIndex = Math.min(fromIndex + safeSize, sorted.size());

        List<CandidateSummaryResponse> items = sorted.subList(fromIndex, toIndex).stream()
                .map(CandidateSummaryResponse::from)
                .toList();

        return new CandidateListResponse(items, sorted.size(), safePage, safeSize);
    }

    // Lấy công ty của Recruiter đang đăng nhập. Cùng cơ chế fallback với
    // UserService.currentAdmin(): SecurityConfig đang permitAll nên chưa chắc có
    // Authentication thật, khi đó dùng tài khoản Recruiter đã seed sẵn.
    private Integer currentRecruiterCompanyId() {
        User recruiterUser = currentRecruiterUser();
        RecruiterProfile profile = recruiterProfileRepository.findByUser_Id(recruiterUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tài khoản này chưa có hồ sơ Recruiter: " + recruiterUser.getEmail()));
        return profile.getCompany().getId();
    }

    private User currentRecruiterUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails details) {
            return details.getUser();
        }
        return userRepository.findUserByEmail(RECRUITER_EMAIL_FALLBACK)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản recruiter"));
    }
}