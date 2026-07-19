package org.ors.subsystem.candidate.job_search.service;

import org.ors.cross.share_kernel.entity.CandidateProfile;
import org.ors.cross.share_kernel.entity.JobPost;
import org.ors.cross.share_kernel.entity.SavedJob;
import org.ors.cross.share_kernel.entity.SavedJobId;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.CandidateProfileRepository;
import org.ors.cross.share_kernel.repository.JobPostRepository;
import org.ors.cross.share_kernel.repository.SavedJobRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

// UC-68, UC-69, UC-70 - Tim kiem tin tuyen dung, luu tin, ung tuyen.
//
// NOI DUY NHAT chua business rule cua phan nay. Controller khong biet luat,
// repository khong biet luat.
//
// Repository dung chung nam o org.ors.cross.share_kernel.repository (30 repository,
// da co san cho ca 32 entity). Inject cai minh can vao day.
@Service
public class JobSearchService implements IJobSearchService {

    private final SavedJobRepository savedJobRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobPostRepository jobPostRepository;

    public JobSearchService(SavedJobRepository savedJobRepository,
                            CandidateProfileRepository candidateProfileRepository,
                            JobPostRepository jobPostRepository) {
        this.savedJobRepository = savedJobRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.jobPostRepository = jobPostRepository;
    }

    // UC-69 Scenario B: Lấy danh sách việc đã lưu.
    @Override
    public List<SavedJob> getSavedJobs(Integer candidateId) {
        return savedJobRepository.findByCandidate_Id(candidateId);
    }

    // UC-69 Scenario A + Toggle: Lưu hoặc Bỏ lưu (Toggle Bookmark).
    // Tài liệu: saved_jobs dùng composite PK (candidate_id, job_post_id),
    // nếu đã tồn tại thì xóa (unsave), nếu chưa thì insert (save).
    @Override
    public String toggleSavedJob(Integer candidateId, Integer jobPostId) {
        return savedJobRepository.findById_CandidateIdAndId_JobPostId(candidateId, jobPostId)
                .map(savedJob -> {
                    savedJobRepository.delete(savedJob);
                    return "Đã xóa khỏi danh sách lưu";
                })
                .orElseGet(() -> {
                    CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Không tìm thấy candidate profile: " + candidateId));
                    JobPost jobPost = jobPostRepository.findById(jobPostId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Không tìm thấy job post: " + jobPostId));

                    SavedJobId savedJobId = new SavedJobId();
                    savedJobId.setCandidateId(candidateId);
                    savedJobId.setJobPostId(jobPostId);

                    SavedJob newSavedJob = new SavedJob();
                    newSavedJob.setId(savedJobId);
                    newSavedJob.setCandidate(candidate);
                    newSavedJob.setJobPost(jobPost);
                    newSavedJob.setSavedAt(Instant.now());
                    savedJobRepository.save(newSavedJob);
                    return "Đã lưu việc làm";
                });
    }

    // UC-68: Tìm kiếm việc làm.
    @Override
    public List<JobPost> searchJobs(String keyword, String category, String location, String type, Double minSalary, String sortBy, int page) {
        // Mặc định phân trang 20 items/page theo tài liệu thiết kế.
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, 20);
        return jobPostRepository.searchJobs(keyword, category, pageable).getContent();
    }

    // UC-68: Xem chi tiết việc làm.
    @Override
    public JobPost getJobDetail(Integer jobPostId) {
        return jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tin tuyển dụng với ID: " + jobPostId));
    }
}
