package org.ors.subsystem.recruiter.recruitment_workflow.service;

import org.springframework.stereotype.Service;

// UC-01..UC-21 - Luong xac minh cong ty va vong doi tin tuyen dung.
//
// NOI DUY NHAT chua business rule cua phan nay. Controller khong biet luat,
// repository khong biet luat.
//
// Repository dung chung nam o org.ors.cross.share_kernel.repository (30 repository,
// da co san cho ca 32 entity). Inject cai minh can vao day.
@Service
public class RecruitmentWorkflowService implements IRecruitmentWorkflowService {

    // TODO: inject repository can dung, vi du:
    // private final JobPostRepository jobPostRepository;

    // TODO: hien thuc cac method cua IRecruitmentWorkflowService.
}
