package org.ors.subsystem.administration.user_management.dto;

import org.ors.cross.share_kernel.entity.RoleName;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.entity.UserStatus;
import org.ors.subsystem.administration.audit.dto.AuditLogResponse;

import java.time.Instant;
import java.util.List;

// DTO cho màn hình xem chi tiết một người dùng (UC-62): hồ sơ + lịch sử thao tác.
// KHÔNG có passwordHash, cùng lý do như UserResponse.
public record UserDetailResponse(
        Integer id,
        String email,
        RoleName role,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt,
        List<AuditLogResponse> history
) {
    public static UserDetailResponse from(User user, List<AuditLogResponse> history) {
        return new UserDetailResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                history
        );
    }
}
