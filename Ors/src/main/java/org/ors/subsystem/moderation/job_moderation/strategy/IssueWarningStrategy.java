package org.ors.subsystem.moderation.job_moderation.strategy;

import org.ors.cross.share_kernel.entity.JobReport;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;
import org.springframework.stereotype.Component;

// Không có side-effect trên entity nào - bản thân ModerationAction + notification (do
// UnderInvestigationState ghi) đã LÀ hành động cảnh cáo.
@Component
public class IssueWarningStrategy implements EnforcementStrategy {

    @Override
    public EnforcementType type() {
        return EnforcementType.ISSUE_WARNING;
    }

    @Override
    public void execute(JobReport report, String summary) {
        // no-op
    }
}
