package org.ors.subsystem.moderation.job_moderation.service;

import org.ors.subsystem.moderation.job_moderation.dto.CloseReportRequest;
import org.ors.subsystem.moderation.job_moderation.dto.ReportDetailResponse;
import org.ors.subsystem.moderation.job_moderation.dto.ReportSummaryResponse;
import org.ors.subsystem.moderation.job_moderation.dto.ResolveReportRequest;

import java.util.List;

// UC-45..48. Controller phụ thuộc interface này, không phụ thuộc ReportService trực tiếp.
public interface IReportService {

    List<ReportSummaryResponse> getQueue(String status, String sort, Integer moderatorId);

    ReportDetailResponse getDetail(Integer reportId);

    void investigate(Integer reportId, Integer moderatorId);

    void resolve(Integer reportId, ResolveReportRequest request, Integer moderatorId);

    void close(Integer reportId, CloseReportRequest request, Integer moderatorId);
}
