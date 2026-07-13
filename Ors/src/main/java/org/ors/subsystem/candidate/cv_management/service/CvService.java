package org.ors.subsystem.candidate.cv_management.service;

import org.springframework.stereotype.Service;

// UC-66, UC-67 - Ho so ca nhan va quan ly CV.
//
// NOI DUY NHAT chua business rule cua phan nay. Controller khong biet luat,
// repository khong biet luat.
//
// Repository dung chung nam o org.ors.cross.share_kernel.repository (30 repository,
// da co san cho ca 32 entity). Inject cai minh can vao day.
@Service
public class CvService implements ICvService {

    // TODO: inject repository can dung, vi du:
    // private final JobPostRepository jobPostRepository;

    // TODO: hien thuc cac method cua ICvService.
}
