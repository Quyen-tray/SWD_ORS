package org.ors.subsystem.moderation.job_moderation.state;

import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.subsystem.moderation.job_moderation.enums.ClosureReason;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;
import org.ors.subsystem.moderation.job_moderation.enums.EntityType;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEvent;

import java.util.List;

public final class PendingState implements ReportState {

    @Override
    public void investigate(ReportContext ctx) {
        ctx.report().setStatus(ReportStatus.UNDER_INVESTIGATION.name());
        ctx.eventPublisher().publish(ModerationEvent.auditOnly(
                ctx.moderator().getId(),
                "INVESTIGATION_STARTED",
                EntityType.REPORT,
                ctx.report().getId(),
                "Investigation started"));
    }

    @Override
    public void resolve(ReportContext ctx, List<EnforcementType> actions, String summary) {
        throw new BadRequestException("Report phải ở trạng thái Under Investigation mới được resolve");
    }

    @Override
    public void close(ReportContext ctx, ClosureReason reason, String note) {
        // AF-01: cho phép đóng nhanh ngay từ PENDING, không bắt buộc phải investigate trước.
        ReportTransitions.close(ctx, reason, note);
    }

    @Override
    public ReportStatus status() {
        return ReportStatus.PENDING;
    }
}
