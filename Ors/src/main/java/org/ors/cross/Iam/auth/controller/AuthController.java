package org.ors.cross.Iam.auth.controller;

import jakarta.validation.Valid;
import org.ors.cross.Iam.auth.dtos.ForgotPasswordRequest;
import org.ors.cross.Iam.auth.dtos.LoginRequest;
import org.ors.cross.Iam.auth.dtos.RegisterRecruiterDTO;
import org.ors.cross.Iam.auth.dtos.ResetPasswordRequest;
import org.ors.cross.Iam.auth.dtos.TokenResponse;
import org.ors.cross.Iam.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<?> registerRecruiter(@RequestBody RegisterRecruiterDTO dto) {
        authService.registerRecruiter(dto);
        return ResponseEntity.ok("Registration accepted. Please check your email for verification link.");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam("email") String email) {
        authService.resendVerification(email);
        return ResponseEntity.ok("Verification email resent if the account exists and is pending.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.requestPasswordReset(request.email());
        return ResponseEntity.ok("If an account exists, a password reset link will be sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.password());
        return ResponseEntity.ok("Password has been reset successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/login/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestParam("code") String code) {
        TokenResponse tokens = authService.googleLogin(code);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
        TokenResponse tokens = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam("refreshToken") String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok("Logged out");
    }
}
