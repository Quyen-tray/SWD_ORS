package org.ors.cross.Iam.auth.dtos;

public record RegisterRecruiterDTO(
        String email,
        String password,
        String confirmPassword,
        String fullName,
        String gender,
        String phoneNumber,
        String companyName,
        String workLocation,
        boolean acceptTerms
) {
}
