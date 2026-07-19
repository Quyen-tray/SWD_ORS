package org.ors.subsystem.moderation.job_moderation.state;

import org.ors.cross.share_kernel.entity.ModerationAction;
import org.ors.subsystem.moderation.job_moderation.enums.ClosureReason;
import org.ors.subsystem.moderation.job_moderation.enums.EntityType;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEvent;

import java.time.Instant;

// Close (UC-48) đi y hệt nhau từ PENDING lẫn UNDER_INVESTIGATION (AF-01: đóng nhanh không
// cần điều tra). Gom vào đây để 2 state không lặp lại cùng một đoạn logic.
final class ReportTransitions {

    private ReportTransitions() {
    }

    static void close(ReportContext ctx, ClosureReason reason, String note) {
        ctx.report().setStatus(ReportStatus.CLOSED.name());

        ModerationAction action = new ModerationAction();
        action.setReport(ctx.report());
        action.setModerator(ctx.moderator());
        action.setActionTaken("CLOSED: " + reason + (note == null || note.isBlank() ? "" : " - " + note));
        // @ColumnDefault("getdate()") không áp dụng khi Hibernate gửi created_at=NULL tường
        // minh cho field chưa set - phải tự set ở đây.
        action.setCreatedAt(Instant.now());
        ctx.moderationActionRepository().save(action);

        ctx.eventPublisher().publish(ModerationEvent.auditOnly(
                ctx.moderator().getId(),
                "REPORT_CLOSED",
                EntityType.REPORT,
                ctx.report().getId(),
                "Report closed - reason=" + reason + (note == null || note.isBlank() ? "" : "; note=" + note)));
    }
}
