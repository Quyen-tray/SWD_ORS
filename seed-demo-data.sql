-- Seed dữ liệu demo cho Admin (FE-07, UC-53..60) + Recruiter (candidate_management/
-- recruitment_workflow, UC-01/04/05/06/07) + Moderation (UC-45..50).
-- Chỉ INSERT (trừ patch PHASE 0 bên dưới, chỉ ADD COLUMN chứ không đụng cột có sẵn).
-- Chạy lại được nhiều lần: xoá sạch các bảng liên quan trước khi seed.
--
-- Dữ liệu bám theo bản thiết kế UI/UX của phần Admin: đủ 4 vai trò
-- (CANDIDATE / RECRUITER / MODERATOR / ADMIN) và đủ 3 trạng thái mà Admin thao tác được
-- (ACTIVE / INACTIVE / BANNED), để mọi nút Activate / Deactivate / Ban đều có case để bấm.
--
-- job_posts được seed để kiểm tra BR-14: danh mục "Information Technology" đang có tin
-- tuyển dụng PUBLISHED trỏ vào, nên phải KHÔNG xoá được; "Human Resources" không có tin
-- nào nên xoá được.
--
-- Phần Recruiter: recruiter_profiles, candidate_profiles, cvs, job_applications,
-- application_status_histories, interviews - đủ dữ liệu để 2 tài khoản RECRUITER đã seed
-- (hong.le@fpt.com.vn - công ty FPT Software, long.vu@tiki.vn - công ty Tiki) login và thấy
-- dữ liệu thật ở cả 8 cột Kanban (UC-04/07) và có sẵn lịch phỏng vấn/kết quả để test UC-05/06.
-- FPT (hong.le) có đủ 8 trạng thái job_applications.status (1 candidate/trạng thái), Tiki
-- (long.vu) chỉ có 2 candidate (SUBMITTED + INTERVIEWED) - đủ để kiểm tra 2 recruiter KHÔNG
-- thấy candidate của nhau (company_id khác nhau, xem RecruiterCandidateService).
--
-- LƯU Ý (Windows): chạy bằng sqlcmd PHẢI thêm "-f 65001" (ép codepage UTF-8), nếu không
-- toàn bộ chuỗi tiếng Việt trong file này sẽ bị lưu sai vào DB (mojibake), kể cả những
-- dòng không liên quan đến Moderation. Ví dụ: sqlcmd -S localhost -U sa -P <pwd> -C -f 65001 -i seed-demo-data.sql
--
-- LƯU Ý (patch PHASE 0): db.sql gốc trong repo (script date 7/14/2026) CHƯA có các cột mà
-- entity Java đã dùng: job_applications.rating/rejection_reason, application_status_histories.
-- reason/changed_by, interviews.status/round/outcome/rating/comments (xem JobApplication.java,
-- ApplicationStatusHistory.java, Interview.java, RecordInterviewOutcomeRequest.java). Block
-- PHASE 0 dưới đây tự thêm các cột này bằng ADD COLUMN có guard IF NOT EXISTS, an toàn khi
-- chạy lại nhiều lần và không lỗi nếu ai đó đã patch tay db.sql trước rồi. Nếu bạn merge patch
-- này thẳng vào db.sql (khuyên dùng), có thể xoá cả block PHASE 0 khỏi file này.

USE OnlineRecruitmentSystem;
GO

-- ============================================================================
-- PATCH "PHASE 0" - bổ sung cột còn thiếu so với entity Java hiện tại.
-- ============================================================================
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.job_applications') AND name = 'rating')
    ALTER TABLE dbo.job_applications ADD rating DECIMAL(3,1) NULL;
GO
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.job_applications') AND name = 'rejection_reason')
    ALTER TABLE dbo.job_applications ADD rejection_reason NVARCHAR(500) NULL;
GO

IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.application_status_histories') AND name = 'reason')
    ALTER TABLE dbo.application_status_histories ADD reason NVARCHAR(500) NULL;
GO
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.application_status_histories') AND name = 'changed_by')
    ALTER TABLE dbo.application_status_histories ADD changed_by INT NULL;
GO
IF NOT EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK_application_status_histories_changed_by')
    ALTER TABLE dbo.application_status_histories
        ADD CONSTRAINT FK_application_status_histories_changed_by FOREIGN KEY (changed_by) REFERENCES dbo.users(id);
GO

IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.interviews') AND name = 'status')
    ALTER TABLE dbo.interviews ADD status NVARCHAR(30) NOT NULL CONSTRAINT DF_interviews_status DEFAULT 'SCHEDULED';
GO
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.interviews') AND name = 'round')
    ALTER TABLE dbo.interviews ADD round INT NOT NULL CONSTRAINT DF_interviews_round DEFAULT 1;
GO
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.interviews') AND name = 'outcome')
    ALTER TABLE dbo.interviews ADD outcome NVARCHAR(30) NULL;
GO
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.interviews') AND name = 'rating')
    ALTER TABLE dbo.interviews ADD rating DECIMAL(3,1) NULL;
GO
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.interviews') AND name = 'comments')
    ALTER TABLE dbo.interviews ADD comments NVARCHAR(MAX) NULL;
GO
IF NOT EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = 'CK_interviews_outcome')
    ALTER TABLE dbo.interviews
        ADD CONSTRAINT CK_interviews_outcome CHECK (outcome IS NULL OR outcome IN ('PASS', 'FAIL', 'NEED_SECOND_ROUND'));
GO

-- ============================================================================
-- Dọn dữ liệu cũ - con trước, cha sau (job_applications có ON DELETE CASCADE xuống
-- application_status_histories/interviews/communications/candidate_evaluations nên xoá
-- job_applications là đủ dọn sạch 4 bảng đó; cvs/candidate_profiles/recruiter_profiles
-- không cascade từ job_applications nên phải xoá tường minh, đúng thứ tự trước
-- companies/users).
-- ============================================================================
DELETE FROM job_applications;
DELETE FROM cvs;
DELETE FROM candidate_profiles;
DELETE FROM recruiter_profiles;
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

-- 9 user CANDIDATE mới (id 8-16) thêm vào cuối - phục vụ riêng phần Recruiter, không đụng
-- tới 7 user gốc của phần Admin/Moderation ở trên.
SET IDENTITY_INSERT users ON;
INSERT INTO users (id, email, password_hash, role, status, created_at, updated_at) VALUES
 (1, N'admin@ors.vn',            @pwd, N'ADMIN',     N'ACTIVE',   '2025-01-01', '2025-01-01'),
 (2, N'quan.tran@gmail.com',     @pwd, N'CANDIDATE', N'ACTIVE',   '2026-03-12', '2026-03-12'),
 (3, N'hong.le@fpt.com.vn',      @pwd, N'RECRUITER', N'ACTIVE',   '2026-02-05', '2026-02-05'),
 (4, N'ducanh.pham@vng.com.vn',  @pwd, N'MODERATOR', N'ACTIVE',   '2026-01-18', '2026-01-18'),
 (5, N'lan.nguyen@gmail.com',    @pwd, N'CANDIDATE', N'BANNED',   '2025-12-22', '2026-01-05'),
 (6, N'bao.dang@gmail.com',      @pwd, N'CANDIDATE', N'INACTIVE', '2026-05-03', '2026-05-20'),
 (7, N'long.vu@tiki.vn',         @pwd, N'RECRUITER', N'ACTIVE',   '2025-11-09', '2025-11-09'),
 (8, N'an.bui@gmail.com',        @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-01', '2026-06-01'),
 (9, N'thao.pham@gmail.com',     @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-02', '2026-06-02'),
 (10, N'kien.hoang@gmail.com',   @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-03', '2026-06-03'),
 (11, N'mai.vu@gmail.com',       @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-04', '2026-06-04'),
 (12, N'tuan.do@gmail.com',      @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-05', '2026-06-05'),
 (13, N'linh.ngo@gmail.com',     @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-06', '2026-06-06'),
 (14, N'huy.dinh@gmail.com',     @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-07', '2026-06-07'),
 (15, N'nga.truong@gmail.com',   @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-08', '2026-06-08'),
 (16, N'phuc.ly@gmail.com',      @pwd, N'CANDIDATE', N'ACTIVE',   '2026-06-09', '2026-06-09');
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

-- ============================================================================
-- Phần Recruiter (candidate_management/recruitment_workflow) - UC-01, 04, 05, 06, 07.
-- ============================================================================

-- recruiter_profiles: bắt buộc phải có, RecruiterCandidateService.currentRecruiterCompanyId()
-- ném ResourceNotFoundException nếu tài khoản RECRUITER chưa có dòng nào ở đây.
SET IDENTITY_INSERT recruiter_profiles ON;
INSERT INTO recruiter_profiles (id, user_id, company_id, full_name, phone_number, gender) VALUES
 (1, 3, 1, N'Lê Thị Hồng',    N'0901234567', N'FEMALE'),
 (2, 7, 2, N'Vũ Thành Long',  N'0912345678', N'MALE');
SET IDENTITY_INSERT recruiter_profiles OFF;
GO

-- candidate_profiles: 8 candidate ứng tuyển vào FPT Software (đủ 8 cột Kanban, mỗi cột 1
-- candidate) + 2 candidate ứng tuyển vào Tiki (kiểm tra cô lập dữ liệu theo company).
SET IDENTITY_INSERT candidate_profiles ON;
INSERT INTO candidate_profiles (id, user_id, full_name, phone_number, avatar_url, updated_at) VALUES
 (1,  2,  N'Trần Anh Quân',   N'0987000001', NULL, DATEADD(DAY, -1, GETDATE())),
 (2,  8,  N'Bùi Thị An',      N'0987000002', NULL, DATEADD(DAY, -3, GETDATE())),
 (3,  9,  N'Phạm Thị Thảo',   N'0987000003', NULL, DATEADD(DAY, -5, GETDATE())),
 (4,  10, N'Hoàng Minh Kiên', N'0987000004', NULL, DATEADD(DAY, -7, GETDATE())),
 (5,  11, N'Vũ Thị Mai',      N'0987000005', NULL, DATEADD(DAY, -10, GETDATE())),
 (6,  12, N'Đỗ Anh Tuấn',     N'0987000006', NULL, DATEADD(DAY, -14, GETDATE())),
 (7,  13, N'Ngô Thuỳ Linh',   N'0987000007', NULL, DATEADD(DAY, -20, GETDATE())),
 (8,  14, N'Đinh Quang Huy',  N'0987000008', NULL, DATEADD(DAY, -12, GETDATE())),
 (9,  15, N'Trương Thị Nga',  N'0987000009', NULL, DATEADD(DAY, -2, GETDATE())),
 (10, 16, N'Lý Gia Phúc',     N'0987000010', NULL, DATEADD(DAY, -8, GETDATE()));
SET IDENTITY_INSERT candidate_profiles OFF;
GO

-- cvs: 1 CV mỗi candidate, đủ cho job_applications.cv_id.
SET IDENTITY_INSERT cvs ON;
INSERT INTO cvs (id, candidate_id, cv_name, file_url, created_at) VALUES
 (1,  1,  N'CV_TranAnhQuan_Backend.pdf',      NULL, DATEADD(DAY, -1, GETDATE())),
 (2,  2,  N'CV_BuiThiAn_Java.pdf',            NULL, DATEADD(DAY, -3, GETDATE())),
 (3,  3,  N'CV_PhamThiThao_Frontend.pdf',     NULL, DATEADD(DAY, -5, GETDATE())),
 (4,  4,  N'CV_HoangMinhKien_Java.pdf',       NULL, DATEADD(DAY, -7, GETDATE())),
 (5,  5,  N'CV_VuThiMai_Backend.pdf',         NULL, DATEADD(DAY, -10, GETDATE())),
 (6,  6,  N'CV_DoAnhTuan_JavaSenior.pdf',     NULL, DATEADD(DAY, -14, GETDATE())),
 (7,  7,  N'CV_NgoThuyLinh_Frontend.pdf',     NULL, DATEADD(DAY, -20, GETDATE())),
 (8,  8,  N'CV_DinhQuangHuy_Backend.pdf',     NULL, DATEADD(DAY, -12, GETDATE())),
 (9,  9,  N'CV_TruongThiNga_Marketing.pdf',   NULL, DATEADD(DAY, -2, GETDATE())),
 (10, 10, N'CV_LyGiaPhuc_Marketing.pdf',      NULL, DATEADD(DAY, -8, GETDATE()));
SET IDENTITY_INSERT cvs OFF;
GO

-- job_applications: application #1-8 vào FPT (job_post 1/2/4, company 1) - 1 application mỗi
-- trạng thái trong 8 trạng thái của job_applications.status (khớp 8 cột Kanban PipelineBoard.jsx).
-- application #9-10 vào Tiki (job_post 3, company 2) - SUBMITTED và INTERVIEWED, để kiểm tra
-- long.vu@tiki.vn KHÔNG thấy 8 application của FPT (khác company_id).
-- rating/rejection_reason chỉ có ở application đã qua phỏng vấn (khớp interview bên dưới).
SET IDENTITY_INSERT job_applications ON;
INSERT INTO job_applications (id, candidate_id, job_post_id, cv_id, status, applied_at, rating, rejection_reason) VALUES
 (1,  1,  1, 1,  N'SUBMITTED',           DATEADD(DAY, -1, GETDATE()),  NULL, NULL),
 (2,  2,  1, 2,  N'UNDER_REVIEW',        DATEADD(DAY, -3, GETDATE()),  NULL, NULL),
 (3,  3,  2, 3,  N'SHORTLISTED',         DATEADD(DAY, -5, GETDATE()),  NULL, NULL),
 (4,  4,  1, 4,  N'INTERVIEW_SCHEDULED', DATEADD(DAY, -7, GETDATE()),  NULL, NULL),
 (5,  5,  4, 5,  N'INTERVIEWED',         DATEADD(DAY, -10, GETDATE()), 4.0,  NULL),
 (6,  6,  1, 6,  N'OFFERED',             DATEADD(DAY, -14, GETDATE()), 4.5,  NULL),
 (7,  7,  2, 7,  N'HIRED',               DATEADD(DAY, -20, GETDATE()), 5.0,  NULL),
 (8,  8,  4, 8,  N'REJECTED',            DATEADD(DAY, -12, GETDATE()), 2.0,  N'Không đủ kinh nghiệm quản lý dự án lớn.'),
 (9,  9,  3, 9,  N'SUBMITTED',           DATEADD(DAY, -2, GETDATE()),  NULL, NULL),
 (10, 10, 3, 10, N'INTERVIEWED',         DATEADD(DAY, -8, GETDATE()),  3.0,  NULL);
SET IDENTITY_INSERT job_applications OFF;
GO

-- application_status_histories: dựng lại đúng chuỗi state active
-- (SUBMITTED -> UNDER_REVIEW -> SHORTLISTED -> INTERVIEW_SCHEDULED -> INTERVIEWED ->
-- OFFERED -> HIRED, REJECTED có thể tới từ bất kỳ trạng thái active nào) cho từng
-- application, để GET /applications/{id}/status-history có dữ liệu thật. Bước SUBMITTED đầu
-- tiên changed_by=NULL (candidate tự nộp đơn), các bước sau changed_by=recruiter tương ứng
-- (3=hong.le cho application FPT, 7=long.vu cho application Tiki).
INSERT INTO application_status_histories (application_id, status, changed_at, reason, changed_by) VALUES
 -- App 1 (Trần Anh Quân, SUBMITTED)
 (1, N'SUBMITTED', DATEADD(DAY, -1, GETDATE()), NULL, NULL),
 -- App 2 (Bùi Thị An, UNDER_REVIEW)
 (2, N'SUBMITTED', DATEADD(DAY, -3, GETDATE()), NULL, NULL),
 (2, N'UNDER_REVIEW', DATEADD(DAY, -2, GETDATE()), NULL, 3),
 -- App 3 (Phạm Thị Thảo, SHORTLISTED)
 (3, N'SUBMITTED', DATEADD(DAY, -5, GETDATE()), NULL, NULL),
 (3, N'UNDER_REVIEW', DATEADD(DAY, -4, GETDATE()), NULL, 3),
 (3, N'SHORTLISTED', DATEADD(DAY, -3, GETDATE()), NULL, 3),
 -- App 4 (Hoàng Minh Kiên, INTERVIEW_SCHEDULED)
 (4, N'SUBMITTED', DATEADD(DAY, -7, GETDATE()), NULL, NULL),
 (4, N'UNDER_REVIEW', DATEADD(DAY, -6, GETDATE()), NULL, 3),
 (4, N'SHORTLISTED', DATEADD(DAY, -5, GETDATE()), NULL, 3),
 (4, N'INTERVIEW_SCHEDULED', DATEADD(DAY, -4, GETDATE()), NULL, 3),
 -- App 5 (Vũ Thị Mai, INTERVIEWED)
 (5, N'SUBMITTED', DATEADD(DAY, -10, GETDATE()), NULL, NULL),
 (5, N'UNDER_REVIEW', DATEADD(DAY, -9, GETDATE()), NULL, 3),
 (5, N'SHORTLISTED', DATEADD(DAY, -8, GETDATE()), NULL, 3),
 (5, N'INTERVIEW_SCHEDULED', DATEADD(DAY, -7, GETDATE()), NULL, 3),
 (5, N'INTERVIEWED', DATEADD(DAY, -5, GETDATE()), NULL, 3),
 -- App 6 (Đỗ Anh Tuấn, OFFERED)
 (6, N'SUBMITTED', DATEADD(DAY, -14, GETDATE()), NULL, NULL),
 (6, N'UNDER_REVIEW', DATEADD(DAY, -13, GETDATE()), NULL, 3),
 (6, N'SHORTLISTED', DATEADD(DAY, -12, GETDATE()), NULL, 3),
 (6, N'INTERVIEW_SCHEDULED', DATEADD(DAY, -11, GETDATE()), NULL, 3),
 (6, N'INTERVIEWED', DATEADD(DAY, -9, GETDATE()), NULL, 3),
 (6, N'OFFERED', DATEADD(DAY, -8, GETDATE()), NULL, 3),
 -- App 7 (Ngô Thuỳ Linh, HIRED - vòng đời đầy đủ)
 (7, N'SUBMITTED', DATEADD(DAY, -20, GETDATE()), NULL, NULL),
 (7, N'UNDER_REVIEW', DATEADD(DAY, -19, GETDATE()), NULL, 3),
 (7, N'SHORTLISTED', DATEADD(DAY, -18, GETDATE()), NULL, 3),
 (7, N'INTERVIEW_SCHEDULED', DATEADD(DAY, -17, GETDATE()), NULL, 3),
 (7, N'INTERVIEWED', DATEADD(DAY, -15, GETDATE()), NULL, 3),
 (7, N'OFFERED', DATEADD(DAY, -14, GETDATE()), NULL, 3),
 (7, N'HIRED', DATEADD(DAY, -10, GETDATE()), NULL, 3),
 -- App 8 (Đinh Quang Huy, REJECTED sau khi phỏng vấn - có reason)
 (8, N'SUBMITTED', DATEADD(DAY, -12, GETDATE()), NULL, NULL),
 (8, N'UNDER_REVIEW', DATEADD(DAY, -11, GETDATE()), NULL, 3),
 (8, N'SHORTLISTED', DATEADD(DAY, -10, GETDATE()), NULL, 3),
 (8, N'INTERVIEW_SCHEDULED', DATEADD(DAY, -9, GETDATE()), NULL, 3),
 (8, N'INTERVIEWED', DATEADD(DAY, -7, GETDATE()), NULL, 3),
 (8, N'REJECTED', DATEADD(DAY, -6, GETDATE()), N'Không đủ kinh nghiệm quản lý dự án lớn.', 3),
 -- App 9 (Trương Thị Nga, Tiki, SUBMITTED)
 (9, N'SUBMITTED', DATEADD(DAY, -2, GETDATE()), NULL, NULL),
 -- App 10 (Lý Gia Phúc, Tiki, INTERVIEWED)
 (10, N'SUBMITTED', DATEADD(DAY, -8, GETDATE()), NULL, NULL),
 (10, N'UNDER_REVIEW', DATEADD(DAY, -7, GETDATE()), NULL, 7),
 (10, N'SHORTLISTED', DATEADD(DAY, -6, GETDATE()), NULL, 7),
 (10, N'INTERVIEW_SCHEDULED', DATEADD(DAY, -5, GETDATE()), NULL, 7),
 (10, N'INTERVIEWED', DATEADD(DAY, -3, GETDATE()), NULL, 7);
GO

-- interviews: 1 lịch phỏng vấn cho mỗi application đã qua giai đoạn INTERVIEW_SCHEDULED trở
-- lên (6 dòng: application 4, 5, 6, 7, 8 bên FPT + application 10 bên Tiki). Application 4
-- (INTERVIEW_SCHEDULED) cố tình để scheduled_time trong TƯƠNG LAI (+3 ngày) và status=
-- 'SCHEDULED', outcome=NULL - để UC-06 (Record Interview Result) có sẵn 1 case CHƯA ghi kết
-- quả mà thử ngay được. 5 dòng còn lại status='COMPLETED' kèm outcome/rating/comments, khớp
-- đúng application.status đã INTERVIEWED trở lên tương ứng.
INSERT INTO interviews (application_id, creator_id, scheduled_time, location, meeting_link, status, round, outcome, rating, comments) VALUES
 (4,  3, DATEADD(DAY, 3, GETDATE()),   N'Phòng họp FPT Tower - Lầu 5', NULL, N'SCHEDULED', 1, NULL,                 NULL, NULL),
 (5,  3, DATEADD(DAY, -5, GETDATE()),  N'Phòng họp FPT Tower - Lầu 5', NULL, N'COMPLETED', 1, N'PASS',              4.0,  N'Nắm vững Node.js, giao tiếp tốt.'),
 (6,  3, DATEADD(DAY, -9, GETDATE()),  N'Phòng họp FPT Tower - Lầu 5', NULL, N'COMPLETED', 1, N'PASS',              4.5,  N'Kinh nghiệm Java vững, phù hợp vị trí Senior.'),
 (7,  3, DATEADD(DAY, -15, GETDATE()), N'Phòng họp FPT Tower - Lầu 5', NULL, N'COMPLETED', 1, N'PASS',              5.0,  N'Xuất sắc, nhận offer ngay sau phỏng vấn.'),
 (8,  3, DATEADD(DAY, -7, GETDATE()),  N'Phòng họp FPT Tower - Lầu 5', NULL, N'COMPLETED', 1, N'FAIL',              2.0,  N'Kỹ năng hệ thống phân tán còn yếu.'),
 (10, 7, DATEADD(DAY, -3, GETDATE()),  N'Văn phòng Tiki - Quận 7',     NULL, N'COMPLETED', 1, N'NEED_SECOND_ROUND', 3.0,  N'Cần thêm 1 vòng phỏng vấn với Trưởng phòng Marketing.');
GO

-- ============================================================================
-- Seed dữ liệu demo cho phần Moderation - Report Moderation (UC-45..50).
-- ============================================================================
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
UNION ALL SELECT 'recruiter_profiles', COUNT(*) FROM recruiter_profiles
UNION ALL SELECT 'candidate_profiles', COUNT(*) FROM candidate_profiles
UNION ALL SELECT 'cvs', COUNT(*) FROM cvs
UNION ALL SELECT 'job_applications', COUNT(*) FROM job_applications
UNION ALL SELECT 'application_status_histories', COUNT(*) FROM application_status_histories
UNION ALL SELECT 'interviews', COUNT(*) FROM interviews
UNION ALL SELECT 'job_reports', COUNT(*) FROM job_reports
UNION ALL SELECT 'moderation_actions', COUNT(*) FROM moderation_actions
UNION ALL SELECT 'audit_logs', COUNT(*) FROM audit_logs;
GO