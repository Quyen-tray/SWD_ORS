package org.ors.subsystem.moderation.job_moderation.security;

// cross.Iam.auth vẫn rỗng (chưa có AuthController/login), SecurityConfig đang permitAll()
// cho toàn bộ ứng dụng. Tách "ai là moderator đang thao tác" ra sau interface này để phần
// còn lại (ReportService, controller...) không biết/không quan tâm login thật hay tạm thời -
// khi có JWT thật, chỉ cần thêm 1 bean @Primary khác implement interface này.
public interface CurrentModeratorResolver {
    Integer resolveModeratorId();
}
