package org.ors.subsystem.recruiter.candidate_management.dto;

import java.util.List;

// Khung phân trang cho UC-01. Khớp với candidateApi.list() bên frontend
// (shared/api/candidateApi.js) vốn kỳ vọng { items, total, page }: thêm pageSize để
// frontend tự tính totalPages mà không cần gọi lại API.
public record CandidateListResponse(
        List<CandidateSummaryResponse> items,
        long total,
        int page,
        int pageSize
) {
}