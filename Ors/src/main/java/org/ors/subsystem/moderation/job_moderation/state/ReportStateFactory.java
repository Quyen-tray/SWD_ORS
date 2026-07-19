package org.ors.subsystem.moderation.job_moderation.state;

import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;

// Factory tĩnh dựng ReportState từ trạng thái hiện tại - cùng tinh thần AccountStates.of()
// bên module Admin. Switch trên enum nên vét cạn: thêm trạng thái mới vào ReportStatus mà
// quên xử lý ở đây thì trình biên dịch báo lỗi ngay.
public final class ReportStateFactory {

    private ReportStateFactory() {
    }

    public static ReportState of(ReportStatus status) {
        if (status == null) {
            throw new BadRequestException("Report không có trạng thái hợp lệ");
        }
        return switch (status) {
            case PENDING -> new PendingState();
            case UNDER_INVESTIGATION -> new UnderInvestigationState();
            case RESOLVED -> new ResolvedState();
            case CLOSED -> new ClosedState();
        };
    }
}
