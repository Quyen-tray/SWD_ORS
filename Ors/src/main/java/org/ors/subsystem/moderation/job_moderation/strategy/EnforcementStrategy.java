package org.ors.subsystem.moderation.job_moderation.strategy;

import org.ors.cross.share_kernel.entity.JobReport;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;

// Strategy pattern (UC-47). Mỗi hành động xử lý report là một class riêng - thêm hành động
// mới = thêm 1 @Component mới, không sửa UnderInvestigationState hay factory này.
public interface EnforcementStrategy {

    EnforcementType type();

    void execute(JobReport report, String summary);
}
