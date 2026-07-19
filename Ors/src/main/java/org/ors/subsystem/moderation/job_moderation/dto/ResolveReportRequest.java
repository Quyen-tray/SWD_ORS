package org.ors.subsystem.moderation.job_moderation.dto;

import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;

import java.util.List;

// UC-47. Validate thủ công trong ReportService (blank summary / rỗng actions) TRƯỚC khi
// đụng repository, không dùng bean validation - giữ nhất quán với cách state-guard hoạt động.
public record ResolveReportRequest(List<EnforcementType> enforcementActions, String resolutionSummary) {
}
