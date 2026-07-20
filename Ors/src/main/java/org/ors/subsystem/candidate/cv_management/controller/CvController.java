package org.ors.subsystem.candidate.cv_management.controller;

import org.ors.cross.share_kernel.entity.CandidateProfile;
import org.ors.cross.share_kernel.entity.Cv;
import org.ors.cross.share_kernel.entity.CvEducation;
import org.ors.cross.share_kernel.entity.CvExperience;
import org.ors.subsystem.candidate.cv_management.service.ICvService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// UC-66, UC-67 - Ho so ca nhan va quan ly CV.
@RestController
@RequestMapping("/candidate/cvs")
public class CvController {

    private final ICvService cvService;

    public CvController(ICvService cvService) {
        this.cvService = cvService;
    }

    // UC-66: Lấy thông tin profile theo userId.
    @GetMapping("/profile/{userId}")
    public ResponseEntity<CandidateProfile> getProfile(@PathVariable Integer userId) {
        return ResponseEntity.ok(cvService.getProfile(userId));
    }

    // UC-66: Cập nhật thông tin profile.
    @PutMapping("/profile/{userId}")
    public ResponseEntity<CandidateProfile> updateProfile(
            @PathVariable Integer userId,
            @RequestBody CandidateProfile profileData) {
        return ResponseEntity.ok(cvService.updateProfile(userId, profileData));
    }

    // UC-67: Lấy danh sách CV.
    @GetMapping("/list/{candidateId}")
    public ResponseEntity<List<Cv>> getCvs(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(cvService.getCvs(candidateId));
    }

    // UC-67: Upload PDF CV.
    @PostMapping("/{candidateId}/upload")
    public ResponseEntity<Cv> uploadCv(
            @PathVariable Integer candidateId,
            @RequestBody CvUploadRequest request) {
        return ResponseEntity.ok(cvService.uploadCv(candidateId, request.getCvName(), request.getFileUrl()));
    }

    // UC-67: Tạo CV online (CV Builder).
    @PostMapping("/{candidateId}/build")
    public ResponseEntity<Cv> createCv(
            @PathVariable Integer candidateId,
            @RequestBody CvBuildRequest request) {
        return ResponseEntity.ok(cvService.createCv(
                candidateId,
                request.getCvName(),
                request.getSkills(),
                request.getEducations(),
                request.getExperiences()
        ));
    }

    // UC-67: Cập nhật CV online.
    @PutMapping("/{cvId}")
    public ResponseEntity<Cv> updateCv(
            @PathVariable Integer cvId,
            @RequestBody CvBuildRequest request) {
        return ResponseEntity.ok(cvService.updateCv(
                cvId,
                request.getCvName(),
                request.getSkills(),
                request.getEducations(),
                request.getExperiences()
        ));
    }

    // UC-67: Xóa CV.
    @DeleteMapping("/{cvId}")
    public ResponseEntity<Void> deleteCv(@PathVariable Integer cvId) {
        cvService.deleteCv(cvId);
        return ResponseEntity.noContent().build();
    }

    // UC-67: Nhân bản CV.
    @PostMapping("/{cvId}/duplicate")
    public ResponseEntity<Cv> duplicateCv(@PathVariable Integer cvId) {
        return ResponseEntity.ok(cvService.duplicateCv(cvId));
    }

    // UC-67: Bật/tắt trạng thái ẩn/hiện.
    @PatchMapping("/{cvId}/visibility")
    public ResponseEntity<Cv> toggleVisibility(@PathVariable Integer cvId) {
        return ResponseEntity.ok(cvService.toggleVisibility(cvId));
    }

    // DTO classes for Request Payload mapping
    public static class CvUploadRequest {
        private String cvName;
        private String fileUrl;

        public String getCvName() { return cvName; }
        public void setCvName(String cvName) { this.cvName = cvName; }

        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    }

    public static class CvBuildRequest {
        private String cvName;
        private List<String> skills;
        private List<CvEducation> educations;
        private List<CvExperience> experiences;

        public String getCvName() { return cvName; }
        public void setCvName(String cvName) { this.cvName = cvName; }

        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }

        public List<CvEducation> getEducations() { return educations; }
        public void setEducations(List<CvEducation> educations) { this.educations = educations; }

        public List<CvExperience> getExperiences() { return experiences; }
        public void setExperiences(List<CvExperience> experiences) { this.experiences = experiences; }
    }
}
