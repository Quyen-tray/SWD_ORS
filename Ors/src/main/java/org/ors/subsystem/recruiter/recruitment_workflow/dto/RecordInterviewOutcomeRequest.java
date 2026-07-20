package org.ors.subsystem.recruiter.recruitment_workflow.dto;

import java.math.BigDecimal;

// Body của POST /interviews/{id}/evaluation (UC-06 Record Interview Result). Khớp
// pipelineApi.recordOutcome(interviewId, { outcome, rating, comments }) bên frontend
// (ENDPOINTS.interviews.recordOutcome). outcome bắt buộc, phải là 1 trong 3 giá trị của
// CK_interviews_outcome (PASS/FAIL/NEED_SECOND_ROUND - xem db.sql patch Phase 0). rating
// (0.0-5.0, khớp cột interviews.rating decimal(3,1) và UI dạng sao trong
// frontend_demo/uc06-record-interview-result.html) và comments đều tuỳ chọn.
public record RecordInterviewOutcomeRequest(
        String outcome,
        BigDecimal rating,
        String comments
) {
}