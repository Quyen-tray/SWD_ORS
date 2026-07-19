package org.ors.subsystem.moderation.job_moderation.state;

import org.ors.cross.share_kernel.entity.ModerationAction;
import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.cross.share_kernel.exception.InternalServerException;
import org.ors.subsystem.moderation.job_moderation.enums.ClosureReason;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;
import org.ors.subsystem.moderation.job_moderation.enums.EntityType;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEvent;
import org.ors.subsystem.moderation.job_moderation.strategy.EnforcementStrategy;

import java.time.Instant;
import java.util.List;

public final class UnderInvestigationState implements ReportState {

    @Override
    public void investigate(ReportContext ctx) {
        throw new BadRequestException("Report đã đang được điều tra");
    }

    @Override
    public void resolve(ReportContext ctx, List<EnforcementType> actions, String summary) {
        // Strategy pattern: mỗi hành động xử lý (AF-01 cho phép chọn nhiều) là một
        // EnforcementStrategy riêng, ReportState không biết chi tiết remove posting hay
        // suspend company làm gì - chỉ biết gọi đúng chiến lược theo type.
        try {
            for (EnforcementType type : actions) {
                EnforcementStrategy strategy = ctx.strategyFactory().resolve(type);
                strategy.execute(ctx.report(), summary);

                ModerationAction action = new ModerationAction();
                action.setReport(ctx.report());
                action.setModerator(ctx.moderator());
                action.setActionTaken(type.name());
                action.setCreatedAt(Instant.now());
                ctx.moderationActionRepository().save(action);
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (RuntimeException e) {
            // EF-02: enforcement thất bại -> giữ nguyên UNDER_INVESTIGATION (không set RESOLVED
            // bên dưới), transaction bao quanh ReportService.resolve() sẽ rollback các thay đổi
            // job_posts/companies mà strategy trước đó đã lỡ ghi.
            throw new InternalServerException("Enforcement action failed. Please retry.");
        }

        ctx.report().setStatus(ReportStatus.RESOLVED.name());
        ctx.eventPublisher().publish(ModerationEvent.withNotification(
                ctx.moderator().getId(),
                "REPORT_RESOLVED",
                EntityType.REPORT,
                ctx.report().getId(),
                "Report resolved - actions=" + actions + "; summary=" + summary,
                ctx.recipientEmail(),
                "Report liên quan đến tin tuyển dụng của bạn đã được xử lý",
                summary));
    }

    @Override
    public void close(ReportContext ctx, ClosureReason reason, String note) {
        ReportTransitions.close(ctx, reason, note);
    }

    @Override
    public ReportStatus status() {
        return ReportStatus.UNDER_INVESTIGATION;
    }
}
