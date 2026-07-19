package org.ors.subsystem.candidate.application_tracking.service;

import org.ors.cross.share_kernel.entity.ApplicationStatusHistory;
import org.ors.cross.share_kernel.repository.ApplicationStatusHistoryRepository;
import org.ors.cross.share_kernel.repository.InterviewRepository;
import org.ors.cross.share_kernel.entity.CandidateProfile;
import org.ors.cross.share_kernel.entity.Cv;
import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.entity.JobPost;
import org.ors.cross.share_kernel.entity.NotificationSetting;
import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.CandidateProfileRepository;
import org.ors.cross.share_kernel.repository.CvRepository;
import org.ors.cross.share_kernel.repository.JobApplicationRepository;
import org.ors.cross.share_kernel.repository.JobPostRepository;
import org.ors.cross.share_kernel.repository.NotificationSettingRepository;
import org.ors.cross.share_kernel.repository.SavedJobRepository;
import org.ors.subsystem.candidate.application_tracking.dto.DashboardStatsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

// UC-70, UC-71, UC-72, UC-73
@Service
public class ApplicationTrackingService implements IApplicationTrackingService {

    private final JobApplicationRepository applicationRepository;
    private final CandidateProfileRepository candidateRepository;
    private final JobPostRepository jobPostRepository;
    private final CvRepository cvRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final SavedJobRepository savedJobRepository;
    private final ApplicationStatusHistoryRepository statusHistoryRepository;
    private final InterviewRepository interviewRepository;

    public ApplicationTrackingService(
            JobApplicationRepository applicationRepository,
            CandidateProfileRepository candidateRepository,
            JobPostRepository jobPostRepository,
            CvRepository cvRepository,
            NotificationSettingRepository notificationSettingRepository,
            SavedJobRepository savedJobRepository,
            ApplicationStatusHistoryRepository statusHistoryRepository,
            InterviewRepository interviewRepository) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobPostRepository = jobPostRepository;
        this.cvRepository = cvRepository;
        this.notificationSettingRepository = notificationSettingRepository;
        this.savedJobRepository = savedJobRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.interviewRepository = interviewRepository;
    }

    // UC-70: Nộp đơn ứng tuyển
    @Override
    @Transactional
    public JobApplication applyForJob(Integer candidateId, Integer jobPostId, Integer cvId) {
        // BR-03: Single Application Limit
        if (applicationRepository.findByCandidate_IdAndJobPost_Id(candidateId, jobPostId).isPresent()) {
            throw new BadRequestException("Bạn đã ứng tuyển cho vị trí này rồi. Mỗi vị trí chỉ được nộp 1 lần.");
        }

        CandidateProfile candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate không tồn tại"));
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Post không tồn tại"));
        Cv cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("CV không tồn tại"));

        // BR-16: Kiểm tra tin tuyển dụng có đang hoạt động không
        if (!"ACTIVE".equals(jobPost.getStatus()) && !"PUBLISHED".equals(jobPost.getStatus())) {
            throw new BadRequestException("Tin tuyển dụng này không ở trạng thái nhận hồ sơ (BR-16).");
        }

        JobApplication application = new JobApplication();
        application.setCandidate(candidate);
        application.setJobPost(jobPost);
        application.setCv(cv);
        application.setStatus("SUBMITTED");
        application.setAppliedAt(Instant.now());

        JobApplication savedApp = applicationRepository.save(application);

        // Ghi nhận lịch sử trạng thái
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        history.setApplication(savedApp);
        history.setStatus("SUBMITTED");
        history.setChangedAt(Instant.now());
        statusHistoryRepository.save(history);

        return savedApp;
    }

    // UC-71: Lấy danh sách lịch sử ứng tuyển
    @Override
    public List<JobApplication> getApplicationsByCandidate(Integer candidateId) {
        return applicationRepository.findByCandidate_Id(candidateId);
    }

    // UC-71: Rút đơn ứng tuyển
    @Override
    @Transactional
    public JobApplication withdrawApplication(Integer applicationId) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn ứng tuyển"));

        // BR-17: Không cho rút nếu đã chốt kết quả
        if ("HIRED".equals(application.getStatus()) || "REJECTED".equals(application.getStatus())) {
            throw new BadRequestException("Không thể rút đơn ứng tuyển đã có kết quả cuối cùng (Hired/Rejected)");
        }
        if ("WITHDRAWN".equals(application.getStatus())) {
            throw new BadRequestException("Đơn này đã được rút rồi");
        }

        application.setStatus("WITHDRAWN");
        JobApplication savedApp = applicationRepository.save(application);

        // Ghi nhận lịch sử trạng thái
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        history.setApplication(savedApp);
        history.setStatus("WITHDRAWN");
        history.setChangedAt(Instant.now());
        statusHistoryRepository.save(history);

        return savedApp;
    }

    // UC-73: Dashboard stats
    @Override
    public DashboardStatsResponse getDashboardStats(Integer candidateId) {
        long totalApplied = applicationRepository.countByCandidate_Id(candidateId);
        
        // Đếm số lịch phỏng vấn sắp tới
        long pendingInterviews = interviewRepository.countByApplication_Candidate_IdAndScheduledTimeAfter(candidateId, Instant.now());
        
        // Đếm số công việc đã lưu
        long savedJobsCount = savedJobRepository.countById_CandidateId(candidateId);

        CandidateProfile profile = candidateRepository.findById(candidateId).orElse(null);
        int profileCompletion = calculateProfileCompletion(profile);

        return new DashboardStatsResponse(totalApplied, pendingInterviews, savedJobsCount, profileCompletion);
    }

    private int calculateProfileCompletion(CandidateProfile profile) {
        if (profile == null) return 0;
        int score = 0;
        if (profile.getFullName() != null && !profile.getFullName().isBlank()) score += 33;
        if (profile.getPhoneNumber() != null && !profile.getPhoneNumber().isBlank()) score += 33;
        if (profile.getAvatarUrl() != null && !profile.getAvatarUrl().isBlank()) score += 34;
        return score;
    }

    // UC-72: Cập nhật settings
    @Override
    @Transactional
    public NotificationSetting updateNotificationSettings(Integer candidateId, NotificationSetting settings) {
        NotificationSetting existing = notificationSettingRepository.findByCandidate_Id(candidateId)
                .orElseGet(() -> {
                    NotificationSetting newSetting = new NotificationSetting();
                    CandidateProfile candidate = candidateRepository.findById(candidateId)
                            .orElseThrow(() -> new ResourceNotFoundException("Candidate không tồn tại"));
                    newSetting.setCandidate(candidate);
                    return newSetting;
                });

        existing.setEmailAlerts(settings.getEmailAlerts());
        existing.setSmsAlerts(settings.getSmsAlerts());
        return notificationSettingRepository.save(existing);
    }
}
