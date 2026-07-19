package org.ors.subsystem.candidate.cv_management.service;

import org.ors.cross.share_kernel.entity.CandidateProfile;
import org.ors.cross.share_kernel.entity.Cv;
import org.ors.cross.share_kernel.entity.CvEducation;
import org.ors.cross.share_kernel.entity.CvExperience;
import java.util.List;

// UC-66, UC-67 - Ho so ca nhan va quan ly CV.
//
// Controller phu thuoc vao interface nay chu khong phu thuoc vao lop hien thuc,
// nen co the thay hoac mock phan hien thuc ma khong dung toi controller.
public interface ICvService {

    // UC-66: Lấy thông tin profile theo userId.
    CandidateProfile getProfile(Integer userId);

    // UC-66: Cập nhật thông tin profile.
    CandidateProfile updateProfile(Integer userId, CandidateProfile updatedData);

    // UC-67: Lấy danh sách CV.
    List<Cv> getCvs(Integer candidateId);

    // UC-67: Upload PDF CV.
    Cv uploadCv(Integer candidateId, String cvName, String fileUrl);

    // UC-67: Tạo CV online (CV Builder).
    Cv createCv(Integer candidateId, String cvName, List<String> skills, List<CvEducation> educations, List<CvExperience> experiences);

    // UC-67: Cập nhật CV online.
    Cv updateCv(Integer cvId, String cvName, List<String> skills, List<CvEducation> educations, List<CvExperience> experiences);

    // UC-67: Xóa CV.
    void deleteCv(Integer cvId);

    // UC-67: Nhân bản CV.
    Cv duplicateCv(Integer cvId);

    // UC-67: Bật/tắt trạng thái ẩn/hiện.
    Cv toggleVisibility(Integer cvId);
}
