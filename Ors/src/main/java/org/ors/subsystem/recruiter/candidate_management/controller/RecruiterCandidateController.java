package org.ors.subsystem.recruiter.candidate_management.controller;

import org.ors.subsystem.recruiter.candidate_management.dto.CandidateListResponse;
import org.ors.subsystem.recruiter.candidate_management.service.IRecruiterCandidateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// UC-01 View Candidate List (candidate_management). Xem 00_KE_HOACH_TONG_QUAN.md để biết
// phạm vi hiện tại (chỉ UC-01, UC-04, UC-05, UC-06, UC-07).
//
// Quy uoc:
//   - Path o day KHONG co /api/v1: context-path da khai bao trong application.properties.
//   - Path phai khop endpoints.js ben frontend (shared/api/endpoints.js: candidates.list).
//   - Controller chi anh xa HTTP sang service, KHONG chua business rule.
@RestController
@RequestMapping("/recruiter/candidates")
public class RecruiterCandidateController {

    private final IRecruiterCandidateService recruiterCandidateService;

    public RecruiterCandidateController(IRecruiterCandidateService recruiterCandidateService) {
        this.recruiterCandidateService = recruiterCandidateService;
    }

    // UC-01 - xem danh sách, kèm tìm kiếm theo từ khoá, lọc theo tin tuyển dụng/trạng
    // thái, phân trang. page mặc định 1, pageSize mặc định 10 (khớp mặc định của
    // Pagination.jsx bên frontend).
    @GetMapping
    public CandidateListResponse getCandidates(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) String jobPostId,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return recruiterCandidateService.getCandidates(keyword, jobPostId, status, page, pageSize);
    }
}