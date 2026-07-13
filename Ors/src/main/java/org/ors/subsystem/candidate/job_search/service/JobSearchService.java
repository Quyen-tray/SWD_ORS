package org.ors.subsystem.candidate.job_search.service;

import org.springframework.stereotype.Service;

// UC-68, UC-69, UC-70 - Tim kiem tin tuyen dung, luu tin, ung tuyen.
//
// NOI DUY NHAT chua business rule cua phan nay. Controller khong biet luat,
// repository khong biet luat.
//
// Repository dung chung nam o org.ors.cross.share_kernel.repository (30 repository,
// da co san cho ca 32 entity). Inject cai minh can vao day.
@Service
public class JobSearchService implements IJobSearchService {

    // TODO: inject repository can dung, vi du:
    // private final JobPostRepository jobPostRepository;

    // TODO: hien thuc cac method cua IJobSearchService.
}
