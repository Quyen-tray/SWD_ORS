package org.ors.subsystem.moderation.job_moderation.service;

import org.ors.cross.share_kernel.entity.JobReport;
import org.ors.cross.share_kernel.repository.CompanyRepository;
import org.ors.cross.share_kernel.repository.JobPostRepository;
import org.ors.cross.share_kernel.repository.JobReportRepository;
import org.ors.subsystem.moderation.job_moderation.dto.DashboardSummaryResponse;
import org.ors.subsystem.moderation.job_moderation.enums.EntityType;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEvent;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

// Facade (UC-49): gộp JobReportRepository + JobPostRepository + CompanyRepository thành
// một getSummary() duy nhất, che độ phức tạp multi-repository khỏi
// ModerationDashboardController. Mỗi panel bọc try/catch riêng (EF-01) - một panel lỗi
// không được làm sập cả 3 panel còn lại.
@Service
public class ModerationDashboardService implements IModerationDashboardService {

    private final JobReportRepository jobReportRepository;
    private final JobPostRepository jobPostRepository;
    private final CompanyRepository companyRepository;
    private final ModerationEventPublisher eventPublisher;

    public ModerationDashboardService(JobReportRepository jobReportRepository,
                                       JobPostRepository jobPostRepository,
                                       CompanyRepository companyRepository,
                                       ModerationEventPublisher eventPublisher) {
        this.jobReportRepository = jobReportRepository;
        this.jobPostRepository = jobPostRepository;
        this.companyRepository = companyRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public DashboardSummaryResponse getSummary(Integer moderatorId) {
        Long openReportsCount = safe(() -> (long) openReports().size());
        Double slaCompliancePercent = safe(this::computeSlaCompliancePercent);
        Long pendingCompanyVerificationCount = safe(() -> companyRepository.countByVerificationStatus("PENDING"));
        Long pendingJobPostingReviewCount = safe(() -> jobPostRepository.countByValidationStatus("PENDING"));

        eventPublisher.publish(ModerationEvent.auditOnly(moderatorId, "DASHBOARD_ACCESS",
                EntityType.REPORT, null, "Moderator accessed the moderation dashboard"));

        return new DashboardSummaryResponse(openReportsCount, slaCompliancePercent,
                pendingCompanyVerificationCount, pendingJobPostingReviewCount);
    }

    private List<JobReport> openReports() {
        return jobReportRepository.findByStatusIn(
                List.of(ReportStatus.PENDING.name(), ReportStatus.UNDER_INVESTIGATION.name()));
    }

    // Định nghĩa SLA% cho slice này: % report đang mở mà CHƯA quá 24h / tổng report đang mở.
    // Đơn giản hoá so với "% report resolve/close trong ngày trong 24h" của bản thiết kế cũ,
    // vì bản cũ cần join audit_logs theo entity - không có cột entity_type/entity_id ở đây.
    private Double computeSlaCompliancePercent() {
        List<JobReport> open = openReports();
        if (open.isEmpty()) {
            return 100.0;
        }
        long withinSla = open.stream().filter(r -> !ReportSlaPolicy.isOverdue(r)).count();
        return (withinSla * 100.0) / open.size();
    }

    private <T> T safe(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException e) {
            return null;
        }
    }
}
