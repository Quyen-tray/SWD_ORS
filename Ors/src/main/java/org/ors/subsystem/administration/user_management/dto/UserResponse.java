package org.ors.subsystem.administration.user_management.dto;

import org.ors.cross.share_kernel.entity.User;

import java.time.Instant;

// DTO trả về cho màn hình quản lý người dùng.
// KHÔNG có passwordHash: mật khẩu đã băm không bao giờ được rời khỏi tầng persistence.
public record UserResponse(
        Integer id,
        String email,
        String role,
        String status,
        Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
