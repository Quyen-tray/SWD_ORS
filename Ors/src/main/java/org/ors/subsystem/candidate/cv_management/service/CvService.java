package org.ors.subsystem.candidate.cv_management.service;

import org.ors.cross.share_kernel.entity.CandidateProfile;
import org.ors.cross.share_kernel.entity.Cv;
import org.ors.cross.share_kernel.entity.CvEducation;
import org.ors.cross.share_kernel.entity.CvExperience;
import org.ors.cross.share_kernel.entity.CvSkill;
import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.CandidateProfileRepository;
import org.ors.cross.share_kernel.repository.CvRepository;
import org.ors.cross.share_kernel.repository.CvSkillRepository;
import org.ors.cross.share_kernel.repository.CvEducationRepository;
import org.ors.cross.share_kernel.repository.CvExperienceRepository;
import org.ors.cross.share_kernel.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

// UC-66, UC-67 - Ho so ca nhan va quan ly CV.
@Service
public class CvService implements ICvService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final CvRepository cvRepository;
    private final CvSkillRepository cvSkillRepository;
    private final CvEducationRepository cvEducationRepository;
    private final CvExperienceRepository cvExperienceRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public CvService(CandidateProfileRepository candidateProfileRepository,
                     CvRepository cvRepository,
                     CvSkillRepository cvSkillRepository,
                     CvEducationRepository cvEducationRepository,
                     CvExperienceRepository cvExperienceRepository,
                     JobApplicationRepository jobApplicationRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.cvRepository = cvRepository;
        this.cvSkillRepository = cvSkillRepository;
        this.cvEducationRepository = cvEducationRepository;
        this.cvExperienceRepository = cvExperienceRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    // UC-66: Lấy thông tin profile theo userId.
    @Override
    public CandidateProfile getProfile(Integer userId) {
        return candidateProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy hồ sơ cho user ID: " + userId));
    }

    // UC-66: Cập nhật thông tin profile (fullName, phoneNumber, avatarUrl).
    @Override
    @Transactional
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

    // UC-67: Lấy danh sách CV.
    @Override
    public List<Cv> getCvs(Integer candidateId) {
        return cvRepository.findByCandidate_Id(candidateId);
    }

    // UC-67: Upload PDF CV.
    @Override
    @Transactional
    public Cv uploadCv(Integer candidateId, String cvName, String fileUrl) {
        List<Cv> existingCvs = cvRepository.findByCandidate_Id(candidateId);
        if (existingCvs.size() >= 5) {
            throw new BadRequestException("Mỗi ứng viên chỉ được có tối đa 5 CV (BR-08).");
        }
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy candidate profile: " + candidateId));
        
        Cv cv = new Cv();
        cv.setCandidate(candidate);
        cv.setCvName(cvName);
        cv.setFileUrl(fileUrl);
        cv.setIsPublic(false);
        cv.setCreatedAt(Instant.now());
        return cvRepository.save(cv);
    }

    // UC-67: Tạo CV online (CV Builder).
    @Override
    @Transactional
    public Cv createCv(Integer candidateId, String cvName, List<String> skills, List<CvEducation> educations, List<CvExperience> experiences) {
        List<Cv> existingCvs = cvRepository.findByCandidate_Id(candidateId);
        if (existingCvs.size() >= 5) {
            throw new BadRequestException("Mỗi ứng viên chỉ được có tối đa 5 CV (BR-08).");
        }
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy candidate profile: " + candidateId));

        Cv cv = new Cv();
        cv.setCandidate(candidate);
        cv.setCvName(cvName);
        cv.setIsPublic(false);
        cv.setCreatedAt(Instant.now());
        Cv savedCv = cvRepository.save(cv);

        if (skills != null) {
            for (String sName : skills) {
                CvSkill cvSkill = new CvSkill();
                cvSkill.setCv(savedCv);
                cvSkill.setSkillName(sName);
                cvSkillRepository.save(cvSkill);
            }
        }
        if (educations != null) {
            for (CvEducation edu : educations) {
                CvEducation newEdu = new CvEducation();
                newEdu.setCv(savedCv);
                newEdu.setSchoolName(edu.getSchoolName());
                newEdu.setMajor(edu.getMajor());
                newEdu.setStartDate(edu.getStartDate());
                newEdu.setEndDate(edu.getEndDate());
                cvEducationRepository.save(newEdu);
            }
        }
        if (experiences != null) {
            for (CvExperience exp : experiences) {
                CvExperience newExp = new CvExperience();
                newExp.setCv(savedCv);
                newExp.setCompanyName(exp.getCompanyName());
                newExp.setPosition(exp.getPosition());
                newExp.setStartDate(exp.getStartDate());
                newExp.setEndDate(exp.getEndDate());
                cvExperienceRepository.save(newExp);
            }
        }
        return savedCv;
    }

    // UC-67: Cập nhật CV online.
    @Override
    @Transactional
    public Cv updateCv(Integer cvId, String cvName, List<String> skills, List<CvEducation> educations, List<CvExperience> experiences) {
        Cv existing = cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CV với ID: " + cvId));

        existing.setCvName(cvName);
        Cv savedCv = cvRepository.save(existing);

        cvSkillRepository.deleteByCv_Id(cvId);
        cvEducationRepository.deleteByCv_Id(cvId);
        cvExperienceRepository.deleteByCv_Id(cvId);

        if (skills != null) {
            for (String sName : skills) {
                CvSkill cvSkill = new CvSkill();
                cvSkill.setCv(savedCv);
                cvSkill.setSkillName(sName);
                cvSkillRepository.save(cvSkill);
            }
        }
        if (educations != null) {
            for (CvEducation edu : educations) {
                CvEducation newEdu = new CvEducation();
                newEdu.setCv(savedCv);
                newEdu.setSchoolName(edu.getSchoolName());
                newEdu.setMajor(edu.getMajor());
                newEdu.setStartDate(edu.getStartDate());
                newEdu.setEndDate(edu.getEndDate());
                cvEducationRepository.save(newEdu);
            }
        }
        if (experiences != null) {
            for (CvExperience exp : experiences) {
                CvExperience newExp = new CvExperience();
                newExp.setCv(savedCv);
                newExp.setCompanyName(exp.getCompanyName());
                newExp.setPosition(exp.getPosition());
                newExp.setStartDate(exp.getStartDate());
                newExp.setEndDate(exp.getEndDate());
                cvExperienceRepository.save(newExp);
            }
        }
        return savedCv;
    }

    // UC-67: Xóa CV.
    @Override
    @Transactional
    public void deleteCv(Integer cvId) {
        if (!cvRepository.existsById(cvId)) {
            throw new ResourceNotFoundException("Không tìm thấy CV với ID: " + cvId);
        }
        // Kiểm tra xem CV có đang liên kết với đơn ứng tuyển nào không
        if (jobApplicationRepository.existsByCv_Id(cvId)) {
            throw new BadRequestException("Không thể xóa CV đang được dùng để ứng tuyển.");
        }
        cvSkillRepository.deleteByCv_Id(cvId);
        cvEducationRepository.deleteByCv_Id(cvId);
        cvExperienceRepository.deleteByCv_Id(cvId);
        cvRepository.deleteById(cvId);
    }

    // UC-67: Nhân bản CV.
    @Override
    @Transactional
    public Cv duplicateCv(Integer cvId) {
        Cv source = cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CV với ID: " + cvId));

        List<Cv> existingCvs = cvRepository.findByCandidate_Id(source.getCandidate().getId());
        if (existingCvs.size() >= 5) {
            throw new BadRequestException("Mỗi ứng viên chỉ được có tối đa 5 CV (BR-08).");
        }

        Cv clone = new Cv();
        clone.setCandidate(source.getCandidate());
        clone.setCvName(source.getCvName() + " (Copy)");
        clone.setFileUrl(source.getFileUrl());
        clone.setIsPublic(false);
        clone.setCreatedAt(Instant.now());
        Cv savedClone = cvRepository.save(clone);

        // Clone skills
        List<CvSkill> skills = cvSkillRepository.findByCv_Id(cvId);
        for (CvSkill s : skills) {
            CvSkill newSkill = new CvSkill();
            newSkill.setCv(savedClone);
            newSkill.setSkillName(s.getSkillName());
            cvSkillRepository.save(newSkill);
        }

        // Clone education
        List<CvEducation> edus = cvEducationRepository.findByCv_Id(cvId);
        for (CvEducation edu : edus) {
            CvEducation newEdu = new CvEducation();
            newEdu.setCv(savedClone);
            newEdu.setSchoolName(edu.getSchoolName());
            newEdu.setMajor(edu.getMajor());
            newEdu.setStartDate(edu.getStartDate());
            newEdu.setEndDate(edu.getEndDate());
            cvEducationRepository.save(newEdu);
        }

        // Clone experience
        List<CvExperience> exps = cvExperienceRepository.findByCv_Id(cvId);
        for (CvExperience exp : exps) {
            CvExperience newExp = new CvExperience();
            newExp.setCv(savedClone);
            newExp.setCompanyName(exp.getCompanyName());
            newExp.setPosition(exp.getPosition());
            newExp.setStartDate(exp.getStartDate());
            newExp.setEndDate(exp.getEndDate());
            cvExperienceRepository.save(newExp);
        }

        return savedClone;
    }

    // UC-67: Bật/tắt trạng thái ẩn/hiện.
    @Override
    @Transactional
    public Cv toggleVisibility(Integer cvId) {
        Cv cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CV với ID: " + cvId));
        cv.setIsPublic(!cv.getIsPublic());
        return cvRepository.save(cv);
    }
}
