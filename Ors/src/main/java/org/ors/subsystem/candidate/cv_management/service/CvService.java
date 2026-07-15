package org.ors.subsystem.candidate.cv_management.service;

import org.ors.cross.share_kernel.entity.CandidateProfile;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.CandidateProfileRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

// UC-66, UC-67 - Ho so ca nhan va quan ly CV.
//
// NOI DUY NHAT chua business rule cua phan nay. Controller khong biet luat,
// repository khong biet luat.
//
// Repository dung chung nam o org.ors.cross.share_kernel.repository (30 repository,
// da co san cho ca 32 entity). Inject cai minh can vao day.
@Service
public class CvService implements ICvService {

    private final CandidateProfileRepository candidateProfileRepository;

    public CvService(CandidateProfileRepository candidateProfileRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
    }

    // UC-66: Lấy thông tin profile theo userId.
    @Override
    public CandidateProfile getProfile(Integer userId) {
        return candidateProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy hồ sơ cho user ID: " + userId));
    }

    // UC-66: Cập nhật thông tin profile (fullName, phoneNumber, avatarUrl).
    // Tài liệu yêu cầu validate: fullName bắt buộc, phoneNumber phải đúng format,
    // avatar phải ≤ 2MB và JPG/PNG/WEBP (BR-09). Phần file upload sẽ thêm sau.
    @Override
    public CandidateProfile updateProfile(Integer userId, CandidateProfile updatedData) {
        CandidateProfile existing = getProfile(userId);

        existing.setFullName(updatedData.getFullName());
        existing.setPhoneNumber(updatedData.getPhoneNumber());
        if (updatedData.getAvatarUrl() != null) {
            existing.setAvatarUrl(updatedData.getAvatarUrl());
        }
        existing.setUpdatedAt(Instant.now());

        return candidateProfileRepository.save(existing);
    }

    // TODO: UC-67 - Implement CV management methods:
    //   - uploadCv(candidateId, file): upload PDF, scan malware (ClamAV), save to cloud
    //   - createCv(candidateId, data): CV builder (education, experience, skills)
    //   - updateCv(cvId, data): edit existing CV
    //   - deleteCv(cvId): delete CV (check linked active applications)
    //   - duplicateCv(cvId): clone CV (BR-08: max 5 CVs)
    //   - toggleVisibility(cvId): toggle is_public for recruiter search index
    //   - downloadCv(cvId): generate pre-signed download URL
}
