package org.ors.subsystem.moderation.job_moderation.service;

import org.springframework.stereotype.Service;

// UC-40..UC-52 - Duyet tin tuyen dung, xu ly bao cao vi pham, nhat ky kiem duyet.
//
// NOI DUY NHAT chua business rule cua phan nay. Controller khong biet luat,
// repository khong biet luat.
//
// Repository dung chung nam o org.ors.cross.share_kernel.repository (30 repository,
// da co san cho ca 32 entity). Inject cai minh can vao day.
@Service
public class JobModerationService implements IJobModerationService {

    // TODO: inject repository can dung, vi du:
    // private final JobPostRepository jobPostRepository;

    // TODO: hien thuc cac method cua IJobModerationService.
}
