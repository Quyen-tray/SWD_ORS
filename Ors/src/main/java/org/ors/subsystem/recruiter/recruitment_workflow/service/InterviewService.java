package org.ors.subsystem.recruiter.recruitment_workflow.service;

import org.ors.cross.Iam.security.user.CustomUserDetails;
import org.ors.cross.share_kernel.entity.Interview;
import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.entity.RecruiterProfile;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.InterviewRepository;
import org.ors.cross.share_kernel.repository.JobApplicationRepository;
import org.ors.cross.share_kernel.repository.RecruiterProfileRepository;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.InterviewResponse;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.RescheduleInterviewRequest;
import org.ors.subsystem.recruiter.recruitment_workflow.dto.ScheduleInterviewRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

// UC-05 Schedule/Reschedule/Cancel Interview (recruitment_workflow). Nơi duy nhất chứa
// business rule của phần này - controller không biết luật, repository không biết luật.
//
// Phạm vi cố ý KHÔNG làm ở đây (xem 00_KE_HOACH_TONG_QUAN.md): đặt/đổi/huỷ lịch phỏng vấn
// KHÔNG tự động đổi job_applications.status - đó là việc riêng của UC-04
// (PATCH /applications/{id}/status, ApplicationStatusController), Recruiter tự bấm chuyển
// trạng thái sang INTERVIEW_SCHEDULED khi cần, giống hệt cách UC-06 (Record Interview
// Result, xem ghi chú trong useInterviewOutcome.js) cũng không đụng vào status. Giữ 2 bảng
// (`job_applications.status` và `interviews.status`) độc lập, mỗi bảng có đúng 1 API sở hữu.
@Service
public class InterviewService implements IInterviewService {

    // Cùng cơ chế fallback với RecruiterCandidateService (UC-01) / ApplicationStatusService
    // (UC-04): màn hình đăng nhập chưa cài đặt thật, SecurityConfig đang permitAll.
    private static final String RECRUITER_EMAIL_FALLBACK = "hong.le@fpt.com.vn";

    private static final List<String> CLOSED_INTERVIEW_STATUSES = List.of("CANCELLED", "COMPLETED");

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public InterviewService(InterviewRepository interviewRepository,
                            JobApplicationRepository jobApplicationRepository,
                            RecruiterProfileRepository recruiterProfileRepository,
                            UserRepository userRepository) {
        this.interviewRepository = interviewRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public InterviewResponse scheduleInterview(ScheduleInterviewRequest request) {
        if (request.applicationId() == null) {
            throw new BadRequestException("Phải chọn đơn ứng tuyển để đặt lịch phỏng vấn");
        }
        Instant scheduledTime = requireFutureTime(request.scheduledTime());

        User recruiterUser = currentRecruiterUser();
        Integer recruiterCompanyId = currentRecruiterCompanyId(recruiterUser);

        // Cùng cách kiểm tra công ty với UC-04 (ApplicationStatusService): 404 thay vì 403
        // khi đơn ứng tuyển không thuộc công ty của Recruiter đang đăng nhập, để không lộ
        // ra là đơn ứng tuyển này có tồn tại hay không.
        JobApplication application = jobApplicationRepository
                .findWithJobPostAndCompanyById(request.applicationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy đơn ứng tuyển: " + request.applicationId()));
        if (!application.getJobPost().getCompany().getId().equals(recruiterCompanyId)) {
            throw new ResourceNotFoundException("Không tìm thấy đơn ứng tuyển: " + request.applicationId());
        }

        // round mới = số interview đã có của đơn này (kể cả đã huỷ) + 1 - Recruiter không
        // tự chọn round, tránh 2 lịch trùng round hoặc round nhảy cóc.
        int nextRound = (int) interviewRepository.countByApplication_Id(application.getId()) + 1;

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setCreator(recruiterUser);
        interview.setScheduledTime(scheduledTime);
        interview.setLocation(request.location());
        interview.setMeetingLink(request.meetingLink());
        interview.setStatus("SCHEDULED");
        interview.setRound(nextRound);
        interviewRepository.save(interview);

        return InterviewResponse.from(interview);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponse getInterview(Integer interviewId) {
        Interview interview = loadOwnedInterview(interviewId);
        return InterviewResponse.from(interview);
    }

    @Override
    @Transactional
    public InterviewResponse cancelInterview(Integer interviewId) {
        Interview interview = loadOwnedInterview(interviewId);
        requireOpenInterview(interview, "huỷ");

        interview.setStatus("CANCELLED");
        interviewRepository.save(interview);
        return InterviewResponse.from(interview);
    }

    @Override
    @Transactional
    public InterviewResponse rescheduleInterview(Integer interviewId, RescheduleInterviewRequest request) {
        Interview interview = loadOwnedInterview(interviewId);
        requireOpenInterview(interview, "đổi lịch");

        Instant newTime = requireFutureTime(request.scheduledTime());
        interview.setScheduledTime(newTime);
        interview.setStatus("RESCHEDULED");
        interviewRepository.save(interview);
        return InterviewResponse.from(interview);
    }

    // Dùng chung cho get/cancel/reschedule: load interview kèm company, 404 nếu không tồn
    // tại hoặc không thuộc công ty của Recruiter đang đăng nhập.
    private Interview loadOwnedInterview(Integer interviewId) {
        Interview interview = interviewRepository.findWithApplicationAndCompanyById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch phỏng vấn: " + interviewId));

        Integer recruiterCompanyId = currentRecruiterCompanyId(currentRecruiterUser());
        if (!interview.getApplication().getJobPost().getCompany().getId().equals(recruiterCompanyId)) {
            throw new ResourceNotFoundException("Không tìm thấy lịch phỏng vấn: " + interviewId);
        }
        return interview;
    }

    // UC-05 A1/A2: không cho đổi lịch hoặc huỷ một lịch đã CANCELLED/COMPLETED - 2 trạng
    // thái đó là kết, không còn thao tác nào hợp lệ tiếp theo.
    private void requireOpenInterview(Interview interview, String action) {
        if (CLOSED_INTERVIEW_STATUSES.contains(interview.getStatus())) {
            throw new BadRequestException(
                    "Không thể " + action + " lịch phỏng vấn đang ở trạng thái " + interview.getStatus());
        }
    }

    // UC-05: thời gian phỏng vấn bắt buộc phải nhập và phải ở tương lai - Recruiter không
    // đặt/đổi lịch vào một thời điểm đã qua.
    private Instant requireFutureTime(Instant scheduledTime) {
        if (scheduledTime == null) {
            throw new BadRequestException("Phải chọn thời gian phỏng vấn");
        }
        if (!scheduledTime.isAfter(Instant.now())) {
            throw new BadRequestException("Thời gian phỏng vấn phải ở tương lai");
        }
        return scheduledTime;
    }

    // Lấy công ty của Recruiter đang đăng nhập - trùng logic với RecruiterCandidateService
    // (UC-01) / ApplicationStatusService (UC-04), lặp lại theo đúng convention hiện có
    // trong project (mỗi service tự có currentRecruiterXxx() riêng) chứ không rút thành
    // lớp dùng chung.
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