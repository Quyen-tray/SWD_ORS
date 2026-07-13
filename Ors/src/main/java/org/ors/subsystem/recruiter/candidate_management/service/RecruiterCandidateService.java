package org.ors.subsystem.recruiter.candidate_management.service;

import org.springframework.stereotype.Service;

// UC-22..UC-32 - Xem, loc, danh gia ung vien ung tuyen; len lich phong van.
//
// NOI DUY NHAT chua business rule cua phan nay. Controller khong biet luat,
// repository khong biet luat.
//
// Repository dung chung nam o org.ors.cross.share_kernel.repository (30 repository,
// da co san cho ca 32 entity). Inject cai minh can vao day.
@Service
public class RecruiterCandidateService implements IRecruiterCandidateService {

    // TODO: inject repository can dung, vi du:
    // private final JobPostRepository jobPostRepository;

    // TODO: hien thuc cac method cua IRecruiterCandidateService.
}
