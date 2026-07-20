package org.ors.subsystem.recruiter.recruitment_workflow.service;

import org.ors.cross.Iam.security.user.CustomUserDetails;
import org.ors.cross.share_kernel.entity.ApplicationStatusHistory;
import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.entity.RecruiterProfile;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.ApplicationStatusHistoryRepository;
import org.ors.cross.share_kernel.repository.JobApplicationRepository;
import org.ors.cross.share_kernel.repository.RecruiterProfileRepository;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.ApplicationStatusHistoryResponse;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.ApplicationStatusResponse;
import org.ors.subsystem.recruiter.recruitment_workflow.state.PipelineState;
import org.ors.subsystem.recruiter.recruitment_workflow.state.PipelineStates;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

// UC-04 Update Pipeline Status + UC-07 Hire/Offer/Reject (dùng lại cùng state - xem
// 00_KE_HOACH_TONG_QUAN.md mục 2.2). Nơi duy nhất chứa business rule của phần này -
// controller không biết luật, repository không biết luật, state chỉ biết "nước đi này
// có hợp lệ không" chứ không biết gì về HTTP hay company của Recruiter.
@Service
public class ApplicationStatusService implements IApplicationStatusService {

    // Cùng cơ chế fallback với RecruiterCandidateService.currentRecruiterUser() (UC-01):
    // màn hình đăng nhập chưa cài đặt thật, SecurityConfig đang permitAll. Khi luồng
    // đăng nhập xong thì nhánh fallback này bỏ đi, phần còn lại giữ nguyên.
    private static final String RECRUITER_EMAIL_FALLBACK = "hong.le@fpt.com.vn";

    private final JobApplicationRepository jobApplicationRepository;
    private final ApplicationStatusHistoryRepository statusHistoryRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public ApplicationStatusService(JobApplicationRepository jobApplicationRepository,
                                    ApplicationStatusHistoryRepository statusHistoryRepository,
                                    RecruiterProfileRepository recruiterProfileRepository,
                                    UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ApplicationStatusResponse updateStatus(Integer applicationId, String targetStatus, String reason) {
        String normalizedTarget = normalizeTarget(targetStatus);

        JobApplication application = jobApplicationRepository.findWithJobPostAndCompanyById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn ứng tuyển: " + applicationId));

        User recruiterUser = currentRecruiterUser();
        Integer recruiterCompanyId = currentRecruiterCompanyId(recruiterUser);

        // Đơn ứng tuyển không thuộc công ty của Recruiter đang đăng nhập: trả 404 thay
        // vì 403 để không lộ ra là đơn ứng tuyển này có tồn tại hay không - cùng cách
        // xử lý với UC-01 (RecruiterCandidateService chỉ tra cứu trong companyId của
        // chính Recruiter, không có nhánh nào lộ dữ liệu công ty khác).
        if (!application.getJobPost().getCompany().getId().equals(recruiterCompanyId)) {
            throw new ResourceNotFoundException("Không tìm thấy đơn ứng tuyển: " + applicationId);
        }

        // State pattern: trạng thái hiện tại tự quyết định nước đi có hợp lệ không.
        // Chỉ có 2 nước đi Recruiter chủ động làm được: đi tiếp đúng 1 bước tuần tự,
        // hoặc từ chối (từ bất kỳ giai đoạn active nào). Xem package .state.
        PipelineState current = PipelineStates.of(application.getStatus());
        PipelineState next;
        if ("REJECTED".equals(normalizedTarget)) {
            requireReason(reason);
            next = current.reject();
        } else {
            next = current.advance();
            if (!next.status().equals(normalizedTarget)) {
                // Nước đi sai ném lỗi NGAY ở đây, không có chuyển trạng thái sai nào
                // được lưu xuống DB - giống nguyên tắc BR-13/NFR-FE07-1 bên AccountState.
                throw new BadRequestException(
                        "Không thể chuyển từ " + current.status() + " sang " + normalizedTarget
                                + " - chỉ có thể đi tiếp tới " + next.status() + " hoặc từ chối (REJECTED)");
            }
        }

        application.setStatus(next.status());
        if ("REJECTED".equals(next.status())) {
            application.setRejectionReason(reason);
        }
        jobApplicationRepository.save(application);

        Instant changedAt = Instant.now();
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        history.setApplication(application);
        history.setStatus(next.status());
        history.setChangedAt(changedAt);
        history.setReason(reason);
        history.setChangedBy(recruiterUser);
        statusHistoryRepository.save(history);

        return new ApplicationStatusResponse(application.getId(), next.status(), changedAt, reason);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationStatusHistoryResponse> getStatusHistory(Integer applicationId) {
        JobApplication application = jobApplicationRepository.findWithJobPostAndCompanyById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn ứng tuyển: " + applicationId));

        Integer recruiterCompanyId = currentRecruiterCompanyId(currentRecruiterUser());
        if (!application.getJobPost().getCompany().getId().equals(recruiterCompanyId)) {
            throw new ResourceNotFoundException("Không tìm thấy đơn ứng tuyển: " + applicationId);
        }

        return statusHistoryRepository.findByApplicationIdOrderByChangedAtDesc(applicationId).stream()
                .map(ApplicationStatusHistoryResponse::from)
                .toList();
    }

    private String normalizeTarget(String targetStatus) {
        if (targetStatus == null || targetStatus.isBlank()) {
            throw new BadRequestException("Phải chọn trạng thái đích");
        }
        return targetStatus.trim().toUpperCase();
    }

    private void requireReason(String reason) {
        // UC-04 Alternative Flow A1/A2: từ chối bắt buộc phải ghi lý do.
        if (reason == null || reason.isBlank()) {
            throw new BadRequestException("Phải nhập lý do khi từ chối ứng viên");
        }
    }

    // Lấy công ty của Recruiter đang đăng nhập - trùng logic với
    // RecruiterCandidateService (UC-01), lặp lại theo đúng convention hiện có trong
    // project (mỗi service tự có currentRecruiterXxx() riêng, xem UserService cũng vậy
    // với currentAdmin()) chứ không rút thành lớp dùng chung.
    private Integer currentRecruiterCompanyId(User recruiterUser) {
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
