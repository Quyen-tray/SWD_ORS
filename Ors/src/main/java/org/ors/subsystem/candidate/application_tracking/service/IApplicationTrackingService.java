package org.ors.subsystem.candidate.application_tracking.service;

import org.ors.cross.share_kernel.entity.JobApplication;
import org.ors.cross.share_kernel.entity.NotificationSetting;
import org.ors.subsystem.candidate.application_tracking.dto.DashboardStatsResponse;

import java.util.List;

// UC-70, UC-71, UC-72, UC-73 - Ung tuyen, theo doi don, thong bao, dashboard.
//
// Controller phu thuoc vao interface nay chu khong phu thuoc vao lop hien thuc,
// nen co the thay hoac mock phan hien thuc ma khong dung toi controller.
public interface IApplicationTrackingService {

    // UC-70: Nộp đơn ứng tuyển (Apply for Job).
    // BR-03: Candidate chỉ được nộp 1 lần cho mỗi job posting.
    JobApplication applyForJob(Integer candidateId, Integer jobPostId, Integer cvId);

    // UC-71 Scenario A: Lấy danh sách lịch sử ứng tuyển.
    List<JobApplication> getApplicationsByCandidate(Integer candidateId);

    // UC-71 Scenario B: Rút đơn ứng tuyển.
    // BR-17: Không cho rút khi status = HIRED hoặc REJECTED.
    JobApplication withdrawApplication(Integer applicationId);

    // UC-73: Lấy thống kê tổng quan cho Candidate Dashboard.
    DashboardStatsResponse getDashboardStats(Integer candidateId);

    // UC-72 Scenario B: Cập nhật cài đặt thông báo (email_alerts, sms_alerts).
    NotificationSetting updateNotificationSettings(Integer candidateId, NotificationSetting settings);
}
