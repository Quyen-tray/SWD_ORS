package org.ors.cross.Iam.auth.service;

import org.ors.cross.Iam.auth.dtos.LoginRequest;
import org.ors.cross.Iam.auth.dtos.RegisterRecruiterDTO;
import org.ors.cross.Iam.auth.dtos.TokenResponse;

public interface AuthService {
    void registerRecruiter(RegisterRecruiterDTO dto);

    void verifyEmail(String token);

    void resendVerification(String email);

    void requestPasswordReset(String email);

    void resetPassword(String token, String newPassword);

    TokenResponse login(LoginRequest request);

    TokenResponse googleLogin(String authorizationCode);

    TokenResponse refreshToken(String refreshToken);

    void logout(String refreshToken);
}
