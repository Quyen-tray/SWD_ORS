package org.ors.subsystem.moderation.job_moderation.service;

import org.ors.cross.share_kernel.entity.AuditLog;
import org.ors.cross.share_kernel.entity.JobPost;
import org.ors.cross.share_kernel.entity.JobReport;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.AuditLogRepository;
import org.ors.cross.share_kernel.repository.JobReportRepository;
import org.ors.cross.share_kernel.repository.ModerationActionRepository;
import org.ors.cross.share_kernel.repository.RecruiterProfileRepository;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.ors.subsystem.moderation.job_moderation.dto.AuditLogResponse;
import org.ors.subsystem.moderation.job_moderation.dto.CloseReportRequest;
import org.ors.subsystem.moderation.job_moderation.dto.ReportDetailResponse;
import org.ors.subsystem.moderation.job_moderation.dto.ReportSummaryResponse;
import org.ors.subsystem.moderation.job_moderation.dto.ResolveReportRequest;
import org.ors.subsystem.moderation.job_moderation.enums.EntityType;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;
import org.ors.subsystem.moderation.job_moderation.event.AuditDescriptionCodec;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEvent;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEventPublisher;
import org.ors.subsystem.moderation.job_moderation.state.ReportContext;
import org.ors.subsystem.moderation.job_moderation.state.ReportStateFactory;
import org.ors.subsystem.moderation.job_moderation.strategy.EnforcementStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

// Nơi duy nhất điều phối State pattern cho vòng đời report (UC-45..48). ReportService
// KHÔNG có if/else theo status - trạng thái hiện tại tự quyết định nước đi hợp lệ
// (xem package state), giống cách UserService bên module Admin dùng AccountStates.
@Service
public class ReportService implements IReportService {

    private final JobReportRepository jobReportRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final ModerationActionRepository moderationActionRepository;
    private final AuditLogRepository auditLogRepository;
    private final EnforcementStrategyFactory strategyFactory;
    private final ModerationEventPublisher eventPublisher;

    public ReportService(JobReportRepository jobReportRepository,
                          UserRepository userRepository,
                          RecruiterProfileRepository recruiterProfileRepository,
                          ModerationActionRepository moderationActionRepository,
                          AuditLogRepository auditLogRepository,
                          EnforcementStrategyFactory strategyFactory,
                          ModerationEventPublisher eventPublisher) {
        this.jobReportRepository = jobReportRepository;
        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.moderationActionRepository = moderationActionRepository;
        this.auditLogRepository = auditLogRepository;
        this.strategyFactory = strategyFactory;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportSummaryResponse> getQueue(String status, String sort, Integer moderatorId) {
        List<JobReport> reports = (status == null || status.isBlank())
                ? jobReportRepository.findAll()
                : jobReportRepository.findByStatusIn(List.of(parseStatus(status).name()));

        Comparator<JobReport> byCreatedAt =
                Comparator.comparing(JobReport::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
        Comparator<JobReport> comparator = "date_asc".equalsIgnoreCase(sort) ? byCreatedAt : byCreatedAt.reversed();

        List<ReportSummaryResponse> result = reports.stream()
                .sorted(comparator)
                .map(this::toSummary)
                .toList();

        eventPublisher.publish(ModerationEvent.auditOnly(moderatorId, "REPORT_QUEUE_ACCESS",
                EntityType.REPORT, null, "Moderator viewed the report queue"));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDetailResponse getDetail(Integer reportId) {
        JobReport report = getReportOrThrow(reportId);

        ReportDetailResponse.JobPostSummary reportedEntity = null;
        boolean entityMissing = false;
        try {
            JobPost jobPost = report.getJobPost();
            reportedEntity = new ReportDetailResponse.JobPostSummary(
                    jobPost.getId(), jobPost.getTitle(), jobPost.getDescription());
        } catch (RuntimeException e) {
            // EF-01: tin tuyển dụng gốc không còn truy cập được - vẫn hiển thị report,
            // chỉ đánh dấu entityMissing thay vì làm hỏng cả trang chi tiết.
            entityMissing = true;
        }

        Integer jobPostId = reportedEntity != null ? reportedEntity.id() : null;
        List<AuditLogResponse> history = auditLogRepository.findAll().stream()
                .map(this::toAuditLogResponse)
                .filter(a -> matchesReportHistory(a, reportId, jobPostId))
                .sorted(Comparator.comparing(AuditLogResponse::createdAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();

        return new ReportDetailResponse(report.getId(), report.getReason(), report.getStatus(),
                report.getCreatedAt(), ReportSlaPolicy.isOverdue(report),
                reportedEntity, entityMissing, history);
    }

    @Override
    @Transactional
    public void investigate(Integer reportId, Integer moderatorId) {
        JobReport report = getReportOrThrow(reportId);
        ReportContext ctx = buildContext(report, moderatorId, null);
        ReportStateFactory.of(parseStatus(report.getStatus())).investigate(ctx);
        jobReportRepository.save(report);
    }

    @Override
    @Transactional
    public void resolve(Integer reportId, ResolveReportRequest request, Integer moderatorId) {
        if (request.enforcementActions() == null || request.enforcementActions().isEmpty()) {
            throw new BadRequestException("Phải chọn ít nhất một hành động xử lý");
        }
        if (request.resolutionSummary() == null || request.resolutionSummary().isBlank()) {
            throw new BadRequestException("Phải nhập tóm tắt xử lý");
        }

        JobReport report = getReportOrThrow(reportId);
        String recipientEmail = resolveRecipientEmail(report);
        ReportContext ctx = buildContext(report, moderatorId, recipientEmail);
        ReportStateFactory.of(parseStatus(report.getStatus()))
                .resolve(ctx, request.enforcementActions(), request.resolutionSummary());
        jobReportRepository.save(report);
    }

    @Override
    @Transactional
    public void close(Integer reportId, CloseReportRequest request, Integer moderatorId) {
        if (request.closureReason() == null) {
            throw new BadRequestException("Phải chọn lý do đóng report");
        }

        JobReport report = getReportOrThrow(reportId);
        ReportContext ctx = buildContext(report, moderatorId, null);
        ReportStateFactory.of(parseStatus(report.getStatus())).close(ctx, request.closureReason(), request.note());
        jobReportRepository.save(report);
    }

    private JobReport getReportOrThrow(Integer reportId) {
        return jobReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy report: " + reportId));
    }

    private ReportContext buildContext(JobReport report, Integer moderatorId, String recipientEmail) {
        User moderator = userRepository.getReferenceById(moderatorId);
        return new ReportContext(report, moderator, recipientEmail, strategyFactory,
                moderationActionRepository, eventPublisher);
    }

    private String resolveRecipientEmail(JobReport report) {
        Integer companyId = report.getJobPost().getCompany().getId();
        List<String> emails = recruiterProfileRepository.findRecruiterEmailsByCompanyId(companyId);
        return emails.isEmpty() ? null : emails.get(0);
    }

    private ReportStatus parseStatus(String raw) {
        try {
            return ReportStatus.valueOf(raw);
        } catch (RuntimeException e) {
            throw new BadRequestException("Report có trạng thái không hợp lệ: " + raw);
        }
    }

    private ReportSummaryResponse toSummary(JobReport report) {
        return new ReportSummaryResponse(
                report.getId(),
                report.getJobPost().getId(),
                report.getJobPost().getTitle(),
                report.getUser().getEmail(),
                report.getCreatedAt(),
                report.getStatus(),
                ReportSlaPolicy.isOverdue(report));
    }

    private AuditLogResponse toAuditLogResponse(AuditLog log) {
        AuditDescriptionCodec.Decoded decoded = AuditDescriptionCodec.decode(log.getDescription());
        return new AuditLogResponse(log.getId(), log.getCreatedAt(), log.getUser().getId(),
                log.getActionType(), decoded.entityType(), decoded.entityId(), decoded.message());
    }

    private boolean matchesReportHistory(AuditLogResponse entry, Integer reportId, Integer jobPostId) {
        if ("REPORT".equalsIgnoreCase(entry.entityType()) && reportId.equals(entry.entityId())) {
            return true;
        }
        return "JOB_POST".equalsIgnoreCase(entry.entityType()) && jobPostId != null && jobPostId.equals(entry.entityId());
    }
}
