package org.ors.subsystem.moderation.job_moderation.state;

import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.subsystem.moderation.job_moderation.enums.ClosureReason;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;

import java.util.List;

public final class ResolvedState implements ReportState {

    @Override
    public void investigate(ReportContext ctx) {
        throw new BadRequestException("RESOLVED là trạng thái kết thúc");
    }

    @Override
    public void resolve(ReportContext ctx, List<EnforcementType> actions, String summary) {
        throw new BadRequestException("RESOLVED là trạng thái kết thúc");
    }

    @Override
    public void close(ReportContext ctx, ClosureReason reason, String note) {
        throw new BadRequestException("RESOLVED là trạng thái kết thúc");
    }

    @Override
    public ReportStatus status() {
        return ReportStatus.RESOLVED;
    }
}
