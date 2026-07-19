package org.ors.subsystem.moderation.job_moderation.strategy;

import org.ors.cross.share_kernel.entity.JobPost;
import org.ors.cross.share_kernel.entity.JobReport;
import org.ors.cross.share_kernel.repository.JobPostRepository;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;
import org.springframework.stereotype.Component;

// job_posts.status không có giá trị 'REMOVED' trong CHECK constraint thật (đã tra
// sys.check_constraints) - dùng 'UNPUBLISHED' (hợp lệ sẵn có) để ẩn tin khỏi Candidate.
@Component
public class RemovePostingStrategy implements EnforcementStrategy {

    private final JobPostRepository jobPostRepository;

    public RemovePostingStrategy(JobPostRepository jobPostRepository) {
        this.jobPostRepository = jobPostRepository;
    }

    @Override
    public EnforcementType type() {
        return EnforcementType.REMOVE_POSTING;
    }

    @Override
    public void execute(JobReport report, String summary) {
        JobPost jobPost = report.getJobPost();
        jobPost.setStatus("UNPUBLISHED");
        jobPostRepository.save(jobPost);
    }
}
