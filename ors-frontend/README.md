# ORS Frontend — Scaffold

Cấu trúc thư mục theo phương pháp COMET (Ch13/Ch15 SWD392): mỗi actor
(Candidate, Recruiter, Moderator, Admin) là **1 User Interaction Subsystem**
riêng dưới `src/features/`, dùng chung tầng `src/shared/` và `src/app/`.

## Cài đặt

```bash
npm install
cp .env.example .env
npm run dev
```

## 4 tầng trong mỗi feature (map theo Detail Design stereotype — COMET)

| Thư mục | Stereotype | Vai trò |
|---|---|---|
| `components/` | `<<boundary>>` | UI, không chứa logic nghiệp vụ |
| `hooks/` | `<<control>>` | Điều phối, validate, gọi nhiều API |
| `api/` | `<<DataWrapper>>` | Giấu chi tiết HTTP (axios), chỉ export hàm nghiệp vụ |
| `types/` | `<<DataAbstraction>>` | JSDoc typedef — không dùng TypeScript, chỉ khai báo type qua JSDoc |

## Trạng thái từng module

**Đầy đủ 4 tầng, có logic thật** (map trực tiếp theo `ors_final.sql` và
`Use_Case_Specification_no_removed_renumbered.docx` đã chốt):
- `features/recruiter/candidate-management`
- `features/recruiter/recruitment-workflow` — có validate "reason bắt buộc khi Reject",
  outcome tách rời khỏi pipeline status, interview status (Cancel/Reschedule)
- `features/recruiter/communication` — có convention parse subject cho Email
- `features/recruiter/dashboard`, `reporting`, `job-management`
- `features/auth/login`
- `features/candidate/job-search`

**Stub — cần hiện thực khi vào đúng sprint:**
- `features/candidate/application-tracking`, `cv-management`
- `features/auth/register`, `forgot-password`, `reset-password`, `change-password`
- `features/moderator/*`, `features/admin/*`

## Quy ước quan trọng

- Không gọi `axios` trực tiếp trong feature — luôn qua `shared/api/httpClient.js`.
- Không đẩy state cục bộ của 1 feature lên `app/store.js` — store toàn cục chỉ chứa
  `auth` và `notifications` (thứ thực sự dùng chung xuyên actor).
- Enum trạng thái (`ApplicationStatus`, `InterviewOutcome`, `InterviewStatus`) khai báo
  1 chỗ duy nhất ở `shared/types/index.js`, không hardcode chuỗi rải rác.
