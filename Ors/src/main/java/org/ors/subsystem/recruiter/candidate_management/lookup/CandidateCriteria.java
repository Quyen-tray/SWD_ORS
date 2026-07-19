package org.ors.subsystem.recruiter.candidate_management.lookup;

import org.ors.cross.share_kernel.exception.BadRequestException;

import java.util.Set;

// Tiêu chí tra cứu ứng viên của UC-01 (xem danh sách, tìm theo từ khoá, lọc theo tin
// tuyển dụng và trạng thái). Gom các tham số rời thành một object để chiến lược nào
// cũng nhận đúng một kiểu đầu vào - giống UserCriteria bên administration.user_management.
//
// companyId KHÔNG đến từ client: nó do service tự suy ra từ Recruiter đang đăng nhập,
// nên criteria luôn giới hạn đúng công ty của Recruiter, không phụ thuộc client có gửi
// đúng hay không.
//
// job_applications.status là NVARCHAR có CHECK constraint (không phải Java enum), nên
// validate ngay tại đây bằng một tập giá trị cố định - client gửi giá trị rác thì bị từ
// chối bằng HTTP 400 thay vì lặng lẽ trả về danh sách rỗng.
public record CandidateCriteria(Integer companyId, String keyword, Integer jobPostId, String status) {

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "SUBMITTED", "UNDER_REVIEW", "SHORTLISTED", "INTERVIEW_SCHEDULED",
            "INTERVIEWED", "OFFERED", "HIRED", "REJECTED", "WITHDRAWN");

    public static CandidateCriteria of(Integer companyId, String keyword, String jobPostId, String status) {
        return new CandidateCriteria(
                companyId,
                trimToNull(keyword),
                parseJobPostId(trimToNull(jobPostId)),
                parseStatus(trimToNull(status)));
    }

    public boolean hasKeyword() {
        return keyword != null;
    }

    public boolean hasJobPostOrStatus() {
        return jobPostId != null || status != null;
    }

    private static Integer parseJobPostId(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Giá trị jobPostId không hợp lệ: " + value);
        }
    }

    private static String parseStatus(String value) {
        if (value == null) {
            return null;
        }
        String upper = value.toUpperCase();
        if (!ALLOWED_STATUSES.contains(upper)) {
            throw new BadRequestException("Giá trị status không hợp lệ: " + value);
        }
        return upper;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}