package org.ors.subsystem.candidate.cv_management.controller;

import org.ors.cross.share_kernel.entity.CandidateProfile;
import org.ors.subsystem.candidate.cv_management.service.ICvService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// UC-66, UC-67 - Ho so ca nhan va quan ly CV.
//
// Quy uoc:
//   - Path o day KHONG co /api/v1: context-path da khai bao trong application.properties.
//   - Path phai khop endpoints.js ben frontend (shared/api/endpoints.js).
//   - Controller chi anh xa HTTP sang service, KHONG chua business rule.
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

    // TODO: UC-67 - Thêm endpoints quản lý CV:
    //   POST   /candidate/cvs/{candidateId}/upload     — Upload PDF CV
    //   POST   /candidate/cvs/{candidateId}/build       — Create CV via builder
    //   PUT    /candidate/cvs/{cvId}                    — Edit CV
    //   DELETE /candidate/cvs/{cvId}                    — Delete CV
    //   POST   /candidate/cvs/{cvId}/duplicate          — Duplicate CV
    //   PATCH  /candidate/cvs/{cvId}/visibility         — Toggle public/private
    //   GET    /candidate/cvs/{cvId}/download           — Download CV PDF
}
