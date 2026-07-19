package org.ors.subsystem.recruiter.candidate_management.state;

import org.ors.cross.share_kernel.exception.BadRequestException;

// 9 giá trị của job_applications.status (CHECK constraint trong db.sql) và nhà máy
// dựng state từ chuỗi status hiện tại của đơn ứng tuyển.
//
// 7 trạng thái "active" tạo thành 1 chuỗi tuần tự - advance() luôn đi đúng 1 bước tới
// trạng thái kế tiếp trong danh sách này, không có nhánh nào khác:
//   SUBMITTED -> UNDER_REVIEW -> SHORTLISTED -> INTERVIEW_SCHEDULED -> INTERVIEWED
//   -> OFFERED -> HIRED
// reject() có thể đi từ bất kỳ trạng thái nào trong 7 trạng thái trên (trừ HIRED, đã
// kết thúc vòng đời) sang REJECTED.
//
// Vì tham số là String (job_applications.status là NVARCHAR có CHECK constraint, không
// phải Java enum - xem CandidateCriteria), nhánh switch dưới đây phải có default ném
// lỗi cho chuỗi lạ, khác với AccountStates (tham số là enum nên switch vét cạn được).
public final class PipelineStates {

    private PipelineStates() {
    }

    public static PipelineState of(String status) {
        if (status == null) {
            throw new BadRequestException("Đơn ứng tuyển không có trạng thái hợp lệ");
        }
        return switch (status) {
            case "SUBMITTED" -> new SubmittedState();
            case "UNDER_REVIEW" -> new UnderReviewState();
            case "SHORTLISTED" -> new ShortlistedState();
            case "INTERVIEW_SCHEDULED" -> new InterviewScheduledState();
            case "INTERVIEWED" -> new InterviewedState();
            case "OFFERED" -> new OfferedState();
            case "HIRED" -> new HiredState();
            case "REJECTED" -> new RejectedState();
            case "WITHDRAWN" -> new WithdrawnState();
            default -> throw new BadRequestException("Trạng thái đơn ứng tuyển không hợp lệ: " + status);
        };
    }

    public static final class SubmittedState implements PipelineState {
        public PipelineState advance() {
            return new UnderReviewState();
        }

        public PipelineState reject() {
            return new RejectedState();
        }

        public String status() {
            return "SUBMITTED";
        }
    }

    public static final class UnderReviewState implements PipelineState {
        public PipelineState advance() {
            return new ShortlistedState();
        }

        public PipelineState reject() {
            return new RejectedState();
        }

        public String status() {
            return "UNDER_REVIEW";
        }
    }

    public static final class ShortlistedState implements PipelineState {
        public PipelineState advance() {
            return new InterviewScheduledState();
        }

        public PipelineState reject() {
            return new RejectedState();
        }

        public String status() {
            return "SHORTLISTED";
        }
    }

    public static final class InterviewScheduledState implements PipelineState {
        public PipelineState advance() {
            return new InterviewedState();
        }

        public PipelineState reject() {
            return new RejectedState();
        }

        public String status() {
            return "INTERVIEW_SCHEDULED";
        }
    }

    public static final class InterviewedState implements PipelineState {
        public PipelineState advance() {
            return new OfferedState();
        }

        public PipelineState reject() {
            return new RejectedState();
        }

        public String status() {
            return "INTERVIEWED";
        }
    }

    public static final class OfferedState implements PipelineState {
        public PipelineState advance() {
            return new HiredState();
        }

        public PipelineState reject() {
            return new RejectedState();
        }

        public String status() {
            return "OFFERED";
        }
    }

    // Kết thúc vòng đời (thành công) - UC-07. Không còn nước đi nào tiếp theo.
    public static final class HiredState implements PipelineState {
        public PipelineState advance() {
            throw new BadRequestException("Đơn ứng tuyển đã được tuyển (Hired), không còn giai đoạn tiếp theo");
        }

        public PipelineState reject() {
            throw new BadRequestException("Không thể từ chối một đơn ứng tuyển đã được tuyển (Hired)");
        }

        public String status() {
            return "HIRED";
        }
    }

    // Kết thúc vòng đời (không thành công). Không có đường quay lại từ Rejected -
    // muốn xem xét lại thì Recruiter tạo lưu ý ngoài hệ thống, UC-04 không hỗ trợ "un-reject".
    public static final class RejectedState implements PipelineState {
        public PipelineState advance() {
            throw new BadRequestException("Đơn ứng tuyển đã bị từ chối, không thể chuyển tiếp");
        }

        public PipelineState reject() {
            throw new BadRequestException("Đơn ứng tuyển đã bị từ chối rồi");
        }

        public String status() {
            return "REJECTED";
        }
    }

    // Candidate tự rút hồ sơ (module candidate.application_tracking, ngoài scope UC-04).
    // Recruiter không có quyền chủ động đẩy đơn vào trạng thái này lẫn đẩy nó đi tiếp
    // sau khi Candidate đã rút - cả 2 nước đi đều bị từ chối tại đây.
    public static final class WithdrawnState implements PipelineState {
        public PipelineState advance() {
            throw new BadRequestException("Ứng viên đã rút hồ sơ, Recruiter không thể thay đổi trạng thái đơn này");
        }

        public PipelineState reject() {
            throw new BadRequestException("Ứng viên đã rút hồ sơ, Recruiter không thể thay đổi trạng thái đơn này");
        }

        public String status() {
            return "WITHDRAWN";
        }
    }
}