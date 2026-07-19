package org.ors.cross.Iam.auth.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
