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

USE OnlineRecruitmentSystem;
GO

DELETE FROM audit_logs;
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

SET IDENTITY_INSERT companies ON;
INSERT INTO companies (id, company_name, verification_status, description, website, created_at) VALUES
 (1, N'FPT Software', N'APPROVED', N'Công ty phần mềm', N'https://fptsoftware.com', '2025-06-01'),
 (2, N'Tiki',         N'APPROVED', N'Sàn thương mại điện tử', N'https://tiki.vn', '2025-06-01');
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
SET IDENTITY_INSERT job_posts ON;
INSERT INTO job_posts (id, company_id, category_id, title, description, status, validation_status, created_at) VALUES
 (1, 1, 1, N'Senior Java Developer',  N'Spring Boot, SQL Server', N'PUBLISHED', N'APPROVED', '2026-06-01'),
 (2, 1, 1, N'Frontend Engineer',       N'React, Vite',             N'PUBLISHED', N'APPROVED', '2026-06-10'),
 (3, 2, 2, N'Digital Marketing Lead',  N'SEO, Ads',                N'PUBLISHED', N'APPROVED', '2026-06-15');
SET IDENTITY_INSERT job_posts OFF;
GO

SELECT 'users' AS bang, COUNT(*) AS so_dong FROM users
UNION ALL SELECT 'companies', COUNT(*) FROM companies
UNION ALL SELECT 'job_categories', COUNT(*) FROM job_categories
UNION ALL SELECT 'job_posts', COUNT(*) FROM job_posts
UNION ALL SELECT 'audit_logs', COUNT(*) FROM audit_logs;
GO
