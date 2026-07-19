package org.ors.subsystem.candidate.cv_management.service;

import org.ors.cross.share_kernel.entity.CandidateProfile;

// UC-66, UC-67 - Ho so ca nhan va quan ly CV.
//
// Controller phu thuoc vao interface nay chu khong phu thuoc vao lop hien thuc,
// nen co the thay hoac mock phan hien thuc ma khong dung toi controller.
public interface ICvService {

    // UC-66: Lấy thông tin profile theo userId.
    CandidateProfile getProfile(Integer userId);

    // UC-66: Cập nhật thông tin profile.
    CandidateProfile updateProfile(Integer userId, CandidateProfile updatedData);

    // TODO: UC-67 - getCvs(), createCv(), updateCv(), deleteCv(), uploadCv(), toggleVisibility()
}
