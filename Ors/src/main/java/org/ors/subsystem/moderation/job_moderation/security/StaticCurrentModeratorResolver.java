package org.ors.subsystem.moderation.job_moderation.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// TODO: khi cross.Iam.auth có AuthController/login thật, thêm bean @Primary khác implement
// CurrentModeratorResolver đọc từ SecurityContextHolder.getContext().getAuthentication()
// (hạ tầng JWT đã có sẵn ở cross.Iam.security.jwt) - không cần sửa ReportService/controller.
@Component
public class StaticCurrentModeratorResolver implements CurrentModeratorResolver {

    private final Integer defaultModeratorId;

    public StaticCurrentModeratorResolver(
            @Value("${app.moderation.default-moderator-id:4}") Integer defaultModeratorId) {
        this.defaultModeratorId = defaultModeratorId;
    }

    @Override
    public Integer resolveModeratorId() {
        return defaultModeratorId;
    }
}
