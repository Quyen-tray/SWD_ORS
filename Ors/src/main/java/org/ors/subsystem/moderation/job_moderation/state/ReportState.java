package org.ors.subsystem.moderation.job_moderation.state;

import org.ors.subsystem.moderation.job_moderation.enums.ClosureReason;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;
import org.ors.subsystem.moderation.job_moderation.enums.ReportStatus;

import java.util.List;

// State pattern cho vòng đời report (UC-45..48). Mỗi trạng thái tự quyết định nước đi nào
// hợp lệ - nước đi sai là một method ném BadRequestException, không phải một nhánh if bị
// thiếu trong ReportService. Thêm trạng thái mới = thêm class mới, không sửa class cũ.
public interface ReportState {

    void investigate(ReportContext ctx);

    void resolve(ReportContext ctx, List<EnforcementType> actions, String summary);

    void close(ReportContext ctx, ClosureReason reason, String note);

    ReportStatus status();
}
