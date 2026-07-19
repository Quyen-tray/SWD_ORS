package org.ors.subsystem.recruiter.candidate_management.service;

import org.ors.subsystem.recruiter.candidate_management.dto.CandidateListResponse;

// UC-01 (candidate_management, scope hiện tại chỉ gồm UC-01, UC-04, UC-05, UC-06, UC-07 -
// xem 00_KE_HOACH_TONG_QUAN.md). UC-02 (Proxy) và các UC khác đã bị loại khỏi phạm vi.
//
// Controller phụ thuộc vào interface này chứ không phụ thuộc vào lớp hiện thực, nên có
// thể thay hoặc mock phần hiện thực mà không đụng tới controller.
public interface IRecruiterCandidateService {

    // UC-01: xem danh sách, tìm theo từ khoá, lọc theo tin tuyển dụng và trạng
    // thái,
    // phân trang - chỉ trong công ty của Recruiter đang đăng nhập. Xem package
    // .lookup
    // (Strategy pattern).
    CandidateListResponse getCandidates(String keyword, String jobPostId, String status,
            int page, int pageSize);
}