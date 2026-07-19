package org.ors.cross.Iam.auth.service.impl;

import org.ors.cross.Iam.auth.dtos.LoginRequest;
import org.ors.cross.Iam.auth.dtos.RegisterRecruiterDTO;
import org.ors.cross.Iam.auth.dtos.TokenResponse;
import org.ors.cross.Iam.auth.email.EmailService;
import org.ors.cross.Iam.auth.service.AuthService;
import org.ors.cross.share_kernel.repository.CompanyRepository;
import org.ors.cross.share_kernel.repository.PasswordResetTokenRepository;
import org.ors.cross.share_kernel.repository.RecruiterProfileRepository;
import org.ors.cross.share_kernel.repository.UserRefreshTokenRepository;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.ors.cross.share_kernel.repository.VerificationTokenRepository;
import org.ors.cross.share_kernel.entity.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ors.cross.share_kernel.entity.PasswordResetToken;
import org.ors.cross.share_kernel.entity.RecruiterProfile;
import org.ors.cross.share_kernel.entity.RoleName;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.entity.UserRefreshToken;
import org.ors.cross.share_kernel.entity.UserStatus;
import org.ors.cross.share_kernel.entity.VerificationToken;
import org.ors.cross.Iam.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final CompanyRepository companyRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           VerificationTokenRepository verificationTokenRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           UserRefreshTokenRepository userRefreshTokenRepository,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder,
                           EmailService emailService,
                           AuthenticationManager authenticationManager,
                           CompanyRepository companyRepository,
                           RecruiterProfileRepository recruiterProfileRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRefreshTokenRepository = userRefreshTokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.companyRepository = companyRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    @Override
    public void registerRecruiter(RegisterRecruiterDTO dto) {
        if (!dto.acceptTerms()) {
            throw new IllegalArgumentException("Terms of service must be accepted.");
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (!dto.password().equals(dto.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (!isPasswordValid(dto.password())) {
            throw new IllegalArgumentException("Password does not meet policy requirements");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setRole(RoleName.RECRUITER);
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);

        Company company = new Company();
        company.setCompanyName(dto.companyName());
        company.setVerificationStatus("DRAFT");
        company.setAddress(dto.workLocation());
        company = companyRepository.save(company);

        RecruiterProfile recruiterProfile = new RecruiterProfile();
        recruiterProfile.setUser(user);
        recruiterProfile.setCompany(company);
        recruiterProfile.setFullName(dto.fullName());
        recruiterProfile.setPhoneNumber(dto.phoneNumber());
        recruiterProfile.setGender(dto.gender());
        recruiterProfileRepository.save(recruiterProfile);

        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
        verificationTokenRepository.save(token);

        emailService.send(
                user.getEmail(),
                "Email Verification",
                "Your verification code is:"
        );
    }

    @Override
    public void verifyEmail(String token) {
        VerificationToken vt = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (vt.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        User user = vt.getUser();
        if (UserStatus.ACTIVE == user.getStatus()) {
            return;
        }

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        verificationTokenRepository.deleteByUser(user);
    }

    @Override
    public void resendVerification(String email) {
        Optional<User> ou = userRepository.findUserByEmail(email);
        if (ou.isEmpty()) return;
        User user = ou.get();
        if (UserStatus.EMAIL_PENDING != user.getStatus()) return;

        verificationTokenRepository.deleteByUser(user);
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
        verificationTokenRepository.save(token);

        emailService.send(
                user.getEmail(),
                "Email Verification",
                "Your verification code is:"
        );
    }

    @Override
    public void requestPasswordReset(String email) {
        Optional<User> ou = userRepository.findUserByEmail(email);
        if (ou.isEmpty()) return;

        User user = ou.get();
        passwordResetTokenRepository.deleteByUser(user);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUser(user);
        prt.setToken(UUID.randomUUID().toString());
        prt.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));
        passwordResetTokenRepository.save(prt);

        log.info("Password reset requested for user {}", user.getEmail());
        emailService.send(
                user.getEmail(),
                "Email Verification",
                "Your verification code is:"
        );
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (prt.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        if (!isPasswordValid(newPassword)) {
            throw new IllegalArgumentException("Password does not meet policy");
        }

        User user = prt.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password reset completed for user {}", user.getEmail());
        passwordResetTokenRepository.deleteByUser(user);
        userRefreshTokenRepository.deleteByUser(user);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findUserByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication"));

        if (UserStatus.ACTIVE != user.getStatus()) {
            throw new IllegalArgumentException("Account is not active. Please verify your email or contact support.");
        }

        String access = jwtService.generateJwtToken(user.getEmail(), user.getRole().name(), user.getId().toString());
        String refresh = jwtService.generateRefreshToken(user.getEmail(), user.getId().toString());

        userRefreshTokenRepository.deleteByUser(user);

        UserRefreshToken urt = new UserRefreshToken();
        urt.setUser(user);
        urt.setToken(refresh);
        urt.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        userRefreshTokenRepository.save(urt);

        return new TokenResponse(access, refresh, jwtService.getAllClaims(access).getExpiration().getTime(), user.getId(), user.getRole().name());
    }

    @Override
    public TokenResponse googleLogin(String authorizationCode) {
        throw new UnsupportedOperationException("Google login not implemented");
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        UserRefreshToken stored = userRefreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expired");
        }

        String email = jwtService.getEmailFromToken(refreshToken);
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newAccess = jwtService.generateJwtToken(user.getEmail(), user.getRole().name(), user.getId().toString());
        String newRefresh = jwtService.generateRefreshToken(user.getEmail(), user.getId().toString());

        userRefreshTokenRepository.deleteByUser(user);
        UserRefreshToken urt = new UserRefreshToken();
        urt.setUser(user);
        urt.setToken(newRefresh);
        urt.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        userRefreshTokenRepository.save(urt);

        return new TokenResponse(newAccess, newRefresh, jwtService.getAllClaims(newAccess).getExpiration().getTime(), user.getId(), user.getRole().name());
    }

    @Override
    public void logout(String refreshToken) {
        userRefreshTokenRepository.findByToken(refreshToken).ifPresent(userRefreshTokenRepository::delete);
    }

    private boolean isPasswordValid(String p) {
        if (p == null || p.length() < 8) return false;

        return true;
    }
}
