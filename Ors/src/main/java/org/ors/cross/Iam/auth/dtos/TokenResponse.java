package org.ors.cross.Iam.auth.dtos;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        Integer userId,
        String role
) {
}
