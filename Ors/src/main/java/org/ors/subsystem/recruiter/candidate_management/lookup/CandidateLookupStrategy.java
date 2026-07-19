package org.ors.subsystem.recruiter.candidate_management.lookup;

import org.ors.cross.share_kernel.entity.JobApplication;

import java.util.List;

// Strategy pattern cho việc tra cứu ứng viên (UC-01).
//
// UC-01 có 3 cách tra cứu khác nhau: tìm theo từ khoá, lọc theo tin tuyển dụng và/hoặc
// trạng thái, hoặc không có tiêu chí nào thì lấy hết ứng viên của công ty. Tách mỗi cách
// thành một class riêng (giống UserLookupStrategy bên administration.user_management):
// thêm cách tra cứu mới (vd lọc theo khoảng ngày nộp đơn) chỉ cần thêm một class mới,
// không sửa class cũ.
//
// Mỗi chiến lược tự trả lời 2 câu: "tiêu chí này có phải việc của tôi không" (supports)
// và "nếu phải thì tra cứu thế nào" (lookup).
public interface CandidateLookupStrategy {

    boolean supports(CandidateCriteria criteria);

    List<JobApplication> lookup(CandidateCriteria criteria);
}