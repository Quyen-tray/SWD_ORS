package org.ors.cross.Iam.auth.service.impl;

import org.junit.jupiter.api.Test;
import org.ors.cross.Iam.auth.dtos.RegisterRecruiterDTO;
import org.ors.cross.Iam.auth.email.EmailService;
import org.ors.cross.Iam.auth.repositories.CompanyRepository;
import org.ors.cross.Iam.auth.repositories.PasswordResetTokenRepository;
import org.ors.cross.Iam.auth.repositories.RecruiterProfileRepository;
import org.ors.cross.Iam.auth.repositories.UserRefreshTokenRepository;
import org.ors.cross.Iam.auth.repositories.UserRepository;
import org.ors.cross.Iam.auth.repositories.VerificationTokenRepository;
import org.ors.cross.Iam.security.jwt.JwtService;
import org.ors.cross.share_kernel.entity.Company;
import org.ors.cross.share_kernel.entity.RecruiterProfile;
import org.ors.cross.share_kernel.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @Test
    void registerRecruiter_shouldCreateCompanyAndRecruiterProfile() {
        UserRepository userRepository = mock(UserRepository.class);
        VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);
        PasswordResetTokenRepository passwordResetTokenRepository = mock(PasswordResetTokenRepository.class);
        UserRefreshTokenRepository userRefreshTokenRepository = mock(UserRefreshTokenRepository.class);
        JwtService jwtService = mock(JwtService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        EmailService emailService = mock(EmailService.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        RecruiterProfileRepository recruiterProfileRepository = mock(RecruiterProfileRepository.class);

        when(userRepository.existsByEmail("recruiter@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123")).thenReturn("encoded-password");

        User savedUser = new User();
        savedUser.setId(101);
        savedUser.setEmail("recruiter@example.com");
        savedUser.setRole("RECRUITER");
        savedUser.setStatus("EMAIL_PENDING");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Company savedCompany = new Company();
        savedCompany.setId(77);
        savedCompany.setCompanyName("Acme Corp");
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);
        when(recruiterProfileRepository.save(any(RecruiterProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthServiceImpl service = new AuthServiceImpl(
                userRepository,
                verificationTokenRepository,
                passwordResetTokenRepository,
                userRefreshTokenRepository,
                jwtService,
                passwordEncoder,
                emailService,
                authenticationManager,
                companyRepository,
                recruiterProfileRepository
        );

        RegisterRecruiterDTO dto = new RegisterRecruiterDTO(
                "recruiter@example.com",
                "Password123",
                "Password123",
                "Nguyen Van A",
                "MALE",
                "0909000000",
                "Acme Corp",
                "Hanoi",
                true
        );

        service.registerRecruiter(dto);

        verify(companyRepository).save(any(Company.class));
        verify(recruiterProfileRepository).save(any(RecruiterProfile.class));
//        verify(emailService).sendVerificationEmail(any(String.class), any(String.class));
    }
}
