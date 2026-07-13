# Base Backend ORS - hướng dẫn cho cả nhóm

Tài liệu này mô tả bộ khung backend và cách mỗi người vào code phần của mình mà không giẫm chân nhau.

Phần **Administration (2.4)** đã code xong và chạy thật - lấy đó làm mẫu tham chiếu cho 4 phần còn lại.

---

## 1. Chia việc theo package

Backend chia đúng theo 5 phần nhóm đã bầu, và **khớp 1-1 với thư mục feature bên frontend** để dễ ghép.

| Phần | Package backend | Thư mục frontend | UC |
|---|---|---|---|
| 2.1.1-4 Recruiter Management | `subsystem.recruiter.job_management`, `subsystem.recruiter.recruitment_workflow` | `features/recruiter/` | UC-01..UC-21 |
| 2.1.5-9 Recruiter Management | `subsystem.recruiter.candidate_management`, `.communication`, `.dashboard`, `.reporting` | `features/recruiter/` | UC-22..UC-35 |
| 2.2 Candidate Management | `subsystem.candidate.*` | `features/candidate/` | UC-63..UC-74 |
| 2.3 Moderation Management | `subsystem.moderation.*` | `features/moderator/` | UC-36..UC-52 |
| 2.4 Administration Management | `subsystem.administration.*` | `features/admin/` | UC-53..UC-60 (ĐÃ XONG) |

Quy tắc: **chỉ sửa file trong package của mình**. Muốn đổi thứ dùng chung (`cross/`) thì báo cả nhóm.

---

## 2. Cấu trúc một module

Mỗi module có 3 tầng, không được nhảy cóc:

```
subsystem/<phần>/<module>/
  controller/   <<boundary>>   chỉ ánh xạ HTTP sang lời gọi service, KHÔNG chứa business rule
  service/      <<control>>    NƠI DUY NHẤT chứa business rule
    I<Tên>Service.java         interface - controller phụ thuộc vào cái này
    <Tên>Service.java          lớp hiện thực
  dto/          <<entity>>     dữ liệu trao đổi với client
```

Repository KHÔNG nằm trong module, mà nằm chung ở `cross/share_kernel/repository/` - vì nhiều phần dùng chung
một entity (ví dụ `JobApplication` thì cả Recruiter lẫn Candidate đều đụng tới).

**Đã có sẵn 30 repository cho toàn bộ 32 entity.** Cần query riêng thì thêm method vào repository tương ứng,
không tạo repository mới trùng entity.

---

## 3. Ba luật bắt buộc

**a) Business rule chỉ nằm ở tầng service.**
Controller không được có `if` nghiệp vụ. Repository chỉ biết truy vấn. Khi luật đổi, chỉ một class đổi.

**b) Controller phụ thuộc interface, không phụ thuộc lớp hiện thực.**
`private final IUserService userService;` chứ không phải `private final UserService userService;`
Nhờ vậy mới mock được service khi viết test.

**c) DTO không được lộ dữ liệu nhạy cảm.**
Xem `administration/user_management/dto/UserResponse.java`: nó **không có** `passwordHash`.
Đừng trả thẳng entity ra ngoài.

---

## 4. Việc đã làm sẵn cho cả nhóm

- **32 entity JPA** trong `cross/share_kernel/entity/` (đã map đúng schema `ors.sql`).
- **30 repository** trong `cross/share_kernel/repository/`.
- **Exception + HTTP status**: ném `BadRequestException` ra HTTP 400, `ResourceNotFoundException` ra HTTP 404.
  `GlobalExceptionHandler` tự bắt, không cần try/catch trong controller.
- **Security + JWT**: `cross/Iam/security/`. Hiện `SecurityConfig` đang để `permitAll()` cho dễ dev.
- **CORS** đã mở cho `http://localhost:5173` (cổng Vite của frontend).

---

## 5. Chạy dự án

**Database.** Chạy `ors.sql` để tạo schema, rồi `codeDemo/seed-demo-data.sql` để có dữ liệu demo.

**Cấu hình bí mật.** Tạo file `Ors/.env` (đã nằm trong `.gitignore`, KHÔNG commit):

```properties
JWT_SECRET=<chuỗi base64 tối thiểu 32 byte>
spring.datasource.username=<user SQL Server>
spring.datasource.password=<mật khẩu>
```

**Chạy backend:** `./mvnw spring-boot:run` (cổng 8080, context-path `/api/v1`).

**Chạy frontend:** trong `ors-frontend/` tạo `.env` với `VITE_API_BASE_URL=http://localhost:8080/api/v1`, rồi `npm run dev`.

---

## 6. Đặt path API

Path trong `@RequestMapping` **không có** `/api/v1` (context-path đã tự thêm).

Path phải khớp `ors-frontend/src/shared/api/endpoints.js` - mọi URL của frontend tập trung ở file đó.
Thêm endpoint mới thì sửa cả hai nơi.

Ví dụ (phần Administration):

| Backend | Frontend gọi |
|---|---|
| `@RequestMapping("/admin/user-management")` | `ENDPOINTS.admin.users.list` |
| `@PutMapping("/{id}/ban")` | `ENDPOINTS.admin.users.ban(id)` |

---

## 7. Mẫu tham chiếu: Administration (2.4)

Phần này đã chạy thật đầu-cuối, đọc để copy cách làm:

| File | Xem để học |
|---|---|
| `administration/user_management/controller/UserController.java` | controller mỏng, không chứa luật |
| `administration/user_management/service/IUserService.java` | tách interface |
| `administration/user_management/service/UserService.java` | nơi thực thi business rule (BR-13) |
| `administration/user_management/state/AccountStates.java` | **State pattern** cho vòng đời trạng thái: mỗi trạng thái một class, nước đi sai thì ném lỗi thay vì thêm nhánh `if` |
| `administration/job_category_management/service/JobCategoryService.java` | kiểm tra ràng buộc **trước khi** ghi (BR-14: không xoá danh mục còn tin tuyển dụng dùng) |
| `administration/audit/AuditLogService.java` | ghi vết thao tác (BR-15) |
| `administration/user_management/dto/UserResponse.java` | DTO che `passwordHash` |

---

## 8. Những chỗ còn nợ (chưa ai làm)

- **`cross/Iam/auth/` vẫn rỗng** - chưa có `AuthController`, nên chưa đăng nhập được. Vì vậy
  `SecurityConfig` mới phải để `permitAll()`, và service tạm lấy `admin@ors.vn` làm người đang đăng nhập
  (xem `UserService.currentAdmin()`). Ai làm auth xong thì bỏ nhánh tạm đó đi.
- **`application.properties` đang chứa mật khẩu Gmail thật** và đã commit lên GitHub. Cần thu hồi
  app-password đó và chuyển sang `.env`.
