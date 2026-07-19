package org.ors.subsystem.moderation.job_moderation.dto;

import org.ors.subsystem.moderation.job_moderation.enums.ClosureReason;

// UC-48.
public record CloseReportRequest(ClosureReason closureReason, String note) {
}
