package org.ors.subsystem.moderation.job_moderation.state;

import org.ors.cross.share_kernel.entity.JobReport;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.repository.ModerationActionRepository;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEventPublisher;
import org.ors.subsystem.moderation.job_moderation.strategy.EnforcementStrategyFactory;

// Gói mọi collaborator mà một ReportState cần để tự thực hiện nước đi của mình (đọc/ghi
// report, tạo ModerationAction, chọn EnforcementStrategy, publish audit event) - state không
// phải nhận từng tham số rời rạc, và ReportService không phải biết state cần gì bên trong.
public record ReportContext(
        JobReport report,
        User moderator,
        String recipientEmail,
        EnforcementStrategyFactory strategyFactory,
        ModerationActionRepository moderationActionRepository,
        ModerationEventPublisher eventPublisher
) {
}
