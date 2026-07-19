-- Seed dữ liệu demo cho phần Admin (FE-07, UC-53..60).
-- Chỉ INSERT, không sửa schema. Chạy lại được nhiều lần: xoá sạch 4 bảng liên quan trước khi seed.
--
-- Dữ liệu bám theo bản thiết kế UI/UX của phần Admin: đủ 4 vai trò
-- (CANDIDATE / RECRUITER / MODERATOR / ADMIN) và đủ 3 trạng thái mà Admin thao tác được
-- (ACTIVE / INACTIVE / BANNED), để mọi nút Activate / Deactivate / Ban đều có case để bấm.
--
-- job_posts được seed để kiểm tra BR-14: danh mục "Information Technology" đang có tin
-- tuyển dụng PUBLISHED trỏ vào, nên phải KHÔNG xoá được; "Human Resources" không có tin
-- nào nên xoá được.
--
-- LƯU Ý (Windows): chạy bằng sqlcmd PHẢI thêm "-f 65001" (ép codepage UTF-8), nếu không
-- toàn bộ chuỗi tiếng Việt trong file này sẽ bị lưu sai vào DB (mojibake), kể cả những
-- dòng không liên quan đến Moderation. Ví dụ: sqlcmd -S localhost -U sa -P <pwd> -C -f 65001 -i seed-demo-data.sql

USE OnlineRecruitmentSystem;
GO

DELETE FROM audit_logs;
DELETE FROM moderation_actions;
DELETE FROM job_reports;
DELETE FROM job_posts;
DELETE FROM job_categories;
DELETE FROM companies;
DELETE FROM users;
GO

-- Mật khẩu của mọi tài khoản demo là "123456" (BCrypt).
DECLARE @pwd NVARCHAR(255) = N'$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi';

SET IDENTITY_INSERT users ON;
INSERT INTO users (id, email, password_hash, role, status, created_at, updated_at) VALUES
 (1, N'admin@ors.vn',            @pwd, N'ADMIN',     N'ACTIVE',   '2025-01-01', '2025-01-01'),
 (2, N'quan.tran@gmail.com',     @pwd, N'CANDIDATE', N'ACTIVE',   '2026-03-12', '2026-03-12'),
 (3, N'hong.le@fpt.com.vn',      @pwd, N'RECRUITER', N'ACTIVE',   '2026-02-05', '2026-02-05'),
 (4, N'ducanh.pham@vng.com.vn',  @pwd, N'MODERATOR', N'ACTIVE',   '2026-01-18', '2026-01-18'),
 (5, N'lan.nguyen@gmail.com',    @pwd, N'CANDIDATE', N'BANNED',   '2025-12-22', '2026-01-05'),
 (6, N'bao.dang@gmail.com',      @pwd, N'CANDIDATE', N'INACTIVE', '2026-05-03', '2026-05-20'),
 (7, N'long.vu@tiki.vn',         @pwd, N'RECRUITER', N'ACTIVE',   '2025-11-09', '2025-11-09');
SET IDENTITY_INSERT users OFF;
GO

-- Company #4 (Grab Vietnam) cố tình để verification_status='PENDING', không gắn job_post
-- nào - chỉ để panel "Pending Company Verification" ở Dashboard (UC-49) có số khác 0.
SET IDENTITY_INSERT companies ON;
INSERT INTO companies (id, company_name, verification_status, description, website, created_at) VALUES
 (1, N'FPT Software',    N'APPROVED', N'Công ty phần mềm', N'https://fptsoftware.com', '2025-06-01'),
 (2, N'Tiki',            N'APPROVED', N'Sàn thương mại điện tử', N'https://tiki.vn', '2025-06-01'),
 (3, N'Shopee Vietnam',  N'APPROVED', N'Sàn thương mại điện tử', N'https://shopee.vn', '2025-07-01'),
 (4, N'Grab Vietnam',    N'PENDING',  N'Nền tảng gọi xe và giao hàng', N'https://grab.com/vn', '2026-06-20');
SET IDENTITY_INSERT companies OFF;
GO

SET IDENTITY_INSERT job_categories ON;
INSERT INTO job_categories (id, category_name) VALUES
 (1, N'Information Technology'),
 (2, N'Marketing'),
 (3, N'Finance & Accounting'),
 (4, N'Human Resources'),
 (5, N'Sales');
SET IDENTITY_INSERT job_categories OFF;
GO

-- Category 1 và 2 đang được tin tuyển dụng dùng -> BR-14 chặn xoá.
-- Category 3, 4, 5 không có tin nào -> xoá được.
-- job_post #6 cố tình để validation_status='PENDING' - để panel "Pending Job Posting
-- Review" ở Dashboard (UC-49) có số khác 0.
SET IDENTITY_INSERT job_posts ON;
INSERT INTO job_posts (id, company_id, category_id, title, description, status, validation_status, created_at) VALUES
 (1, 1, 1, N'Senior Java Developer',  N'Spring Boot, SQL Server', N'PUBLISHED', N'APPROVED', '2026-06-01'),
 (2, 1, 1, N'Frontend Engineer',       N'React, Vite',             N'PUBLISHED', N'APPROVED', '2026-06-10'),
 (3, 2, 2, N'Digital Marketing Lead',  N'SEO, Ads',                N'PUBLISHED', N'APPROVED', '2026-06-15'),
 (4, 1, 1, N'Backend Engineer',        N'Node.js, PostgreSQL',     N'PUBLISHED', N'APPROVED', '2026-06-20'),
 (5, 3, 5, N'Sales Executive',         N'B2B sales, CRM tools',    N'PUBLISHED', N'APPROVED', '2026-06-25'),
 (6, 2, 3, N'Financial Analyst',       N'Excel, forecasting models', N'DRAFT',  N'PENDING',  '2026-07-05');
SET IDENTITY_INSERT job_posts OFF;
GO

-- Seed dữ liệu demo cho phần Moderation - Report Moderation (UC-45..50).
-- Phủ đủ 4 trạng thái của job_reports.status, dàn trải trên nhiều job_post/company để
-- Report Queue/Audit Log không trống trải khi demo. Report #1 và #7 cố tình tạo cách đây
-- 72h để demo cờ "quá hạn SLA" (BR-06) - dùng mốc rộng (72h thay vì sát ngưỡng 24h) vì
-- GETDATE() (giờ server SQL) và Instant.now() (giờ JVM) đã quan sát thấy lệch nhau vài giờ
-- trên máy dev, mốc rộng để chắc chắn vẫn overdue dù có lệch giờ.
SET IDENTITY_INSERT job_reports ON;
INSERT INTO job_reports (id, user_id, job_post_id, reason, status, created_at) VALUES
 (1,  2, 1, N'Tin đăng yêu cầu ứng viên nộp phí trước khi phỏng vấn, nghi ngờ lừa đảo.', N'PENDING', DATEADD(HOUR, -72, GETDATE())),
 (2,  6, 2, N'Mô tả công việc không khớp với thông tin đã trao đổi qua email.', N'PENDING', DATEADD(HOUR, -2, GETDATE())),
 (3,  5, 5, N'Tin đăng yêu cầu đặt cọc thiết bị làm việc trước khi ký hợp đồng.', N'PENDING', DATEADD(HOUR, -5, GETDATE())),
 (4,  2, 4, N'Yêu cầu công việc không rõ ràng, nghi ngờ tin tuyển dụng giả.', N'PENDING', DATEADD(HOUR, -1, GETDATE())),
 (5,  2, 3, N'Ngôn từ trong tin đăng mang tính phân biệt đối xử.', N'UNDER_INVESTIGATION', DATEADD(HOUR, -10, GETDATE())),
 (6,  6, 5, N'Mức lương ghi trong tin không khớp với thực tế lúc phỏng vấn.', N'UNDER_INVESTIGATION', DATEADD(HOUR, -6, GETDATE())),
 (7,  5, 1, N'Nghi ngờ tin tuyển dụng ảo để thu thập hồ sơ ứng viên.', N'UNDER_INVESTIGATION', DATEADD(HOUR, -72, GETDATE())),
 (8,  6, 1, N'Công ty yêu cầu ứng viên đặt cọc tiền trước khi nhận việc.', N'RESOLVED', DATEADD(DAY, -2, GETDATE())),
 (9,  2, 2, N'Phát hiện tin đăng trùng lặp với công ty khác.', N'RESOLVED', DATEADD(DAY, -4, GETDATE())),
 (10, 5, 4, N'Từ chối phỏng vấn nhưng vẫn yêu cầu phí hồ sơ.', N'RESOLVED', DATEADD(DAY, -1, GETDATE())),
 (11, 2, 2, N'Report này trùng với report khác đã xử lý trước đó.', N'CLOSED', DATEADD(DAY, -3, GETDATE())),
 (12, 6, 5, N'Người report rút lại khiếu nại sau khi trao đổi trực tiếp với nhà tuyển dụng.', N'CLOSED', DATEADD(DAY, -5, GETDATE()));
SET IDENTITY_INSERT job_reports OFF;
GO

-- Report #8 REMOVE_POSTING, #9 SUSPEND_COMPANY, #10 ISSUE_WARNING - đủ cả 3 EnforcementType.
SET IDENTITY_INSERT moderation_actions ON;
INSERT INTO moderation_actions (id, report_id, moderator_id, action_taken, created_at) VALUES
 (1, 8,  4, N'REMOVE_POSTING',  DATEADD(DAY, -1, GETDATE())),
 (2, 9,  4, N'SUSPEND_COMPANY', DATEADD(DAY, -3, GETDATE())),
 (3, 10, 4, N'ISSUE_WARNING',   DATEADD(HOUR, -20, GETDATE()));
SET IDENTITY_INSERT moderation_actions OFF;
GO

-- audit_logs không có cột entity_type/entity_id riêng - Slice B tự nhồi 2 giá trị này vào
-- đầu description theo định dạng "[entityType=X;entityId=Y] ..." (xem AuditDescriptionCodec),
-- để UC-46 (lịch sử kiểm duyệt) và UC-50 (audit log) có sẵn dữ liệu mà không cần bấm nút trước.
INSERT INTO audit_logs (user_id, action_type, description, created_at) VALUES
 (4, N'INVESTIGATION_STARTED', N'[entityType=REPORT;entityId=5] Investigation started', DATEADD(HOUR, -9, GETDATE())),
 (4, N'INVESTIGATION_STARTED', N'[entityType=REPORT;entityId=6] Investigation started', DATEADD(HOUR, -5, GETDATE())),
 (4, N'INVESTIGATION_STARTED', N'[entityType=REPORT;entityId=7] Investigation started', DATEADD(HOUR, -71, GETDATE())),
 (4, N'INVESTIGATION_STARTED', N'[entityType=REPORT;entityId=8] Investigation started', DATEADD(DAY, -2, GETDATE())),
 (4, N'REPORT_RESOLVED', N'[entityType=REPORT;entityId=8] Report resolved - actions=[REMOVE_POSTING]; summary=Removed after verifying the payment request was fraudulent.', DATEADD(DAY, -1, GETDATE())),
 (4, N'INVESTIGATION_STARTED', N'[entityType=REPORT;entityId=9] Investigation started', DATEADD(DAY, -4, GETDATE())),
 (4, N'REPORT_RESOLVED', N'[entityType=REPORT;entityId=9] Report resolved - actions=[SUSPEND_COMPANY]; summary=Confirmed duplicate posting under a shell company, suspended for review.', DATEADD(DAY, -3, GETDATE())),
 (4, N'INVESTIGATION_STARTED', N'[entityType=REPORT;entityId=10] Investigation started', DATEADD(DAY, -1, GETDATE())),
 (4, N'REPORT_RESOLVED', N'[entityType=REPORT;entityId=10] Report resolved - actions=[ISSUE_WARNING]; summary=First offense, issued a formal warning to the recruiter.', DATEADD(HOUR, -20, GETDATE())),
 (4, N'REPORT_CLOSED', N'[entityType=REPORT;entityId=11] Report closed - reason=DUPLICATE_REPORT', DATEADD(DAY, -3, GETDATE())),
 (4, N'REPORT_CLOSED', N'[entityType=REPORT;entityId=12] Report closed - reason=REPORTER_ERROR; note=Reporter withdrew after talking directly with the recruiter.', DATEADD(DAY, -5, GETDATE())),
 (4, N'REPORT_QUEUE_ACCESS', N'Moderator viewed the report queue', DATEADD(HOUR, -3, GETDATE())),
 (4, N'DASHBOARD_ACCESS', N'Moderator accessed the moderation dashboard', DATEADD(HOUR, -1, GETDATE()));
GO

SELECT 'users' AS bang, COUNT(*) AS so_dong FROM users
UNION ALL SELECT 'companies', COUNT(*) FROM companies
UNION ALL SELECT 'job_categories', COUNT(*) FROM job_categories
UNION ALL SELECT 'job_posts', COUNT(*) FROM job_posts
UNION ALL SELECT 'job_reports', COUNT(*) FROM job_reports
UNION ALL SELECT 'moderation_actions', COUNT(*) FROM moderation_actions
UNION ALL SELECT 'audit_logs', COUNT(*) FROM audit_logs;
GO
