package org.ors.subsystem.administration.job_category_management.service;

import org.ors.cross.Iam.security.user.CustomUserDetails;
import org.ors.cross.share_kernel.entity.JobCategory;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.JobCategoryRepository;
import org.ors.cross.share_kernel.repository.JobPostRepository;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.ors.subsystem.administration.audit.AuditLogService;
import org.ors.subsystem.administration.job_category_management.dto.JobCategoryRequest;
import org.ors.subsystem.administration.job_category_management.dto.JobCategoryResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Nơi duy nhất BR-14 được thực thi.
@Service
public class JobCategoryService implements IJobCategoryService {

    private static final String ADMIN_EMAIL_FALLBACK = "admin@ors.vn";

    // Tin tuyển dụng đã đăng thì đang thực sự dùng danh mục. Chỉ trạng thái này mới coi là
    // "đang hoạt động" theo BR-14; DRAFT hay CLOSED thì không chặn xoá.
    private static final String ACTIVE_JOB_POST_STATUS = "PUBLISHED";

    private final JobCategoryRepository jobCategoryRepository;
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public JobCategoryService(JobCategoryRepository jobCategoryRepository,
                              JobPostRepository jobPostRepository,
                              UserRepository userRepository,
                              AuditLogService auditLogService) {
        this.jobCategoryRepository = jobCategoryRepository;
        this.jobPostRepository = jobPostRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public List<JobCategoryResponse> getAllJobCategories() {
        return jobCategoryRepository.findAll().stream().map(JobCategoryResponse::from).toList();
    }

    @Override
    @Transactional
    public JobCategoryResponse createJobCategory(JobCategoryRequest request) {
        String name = requireName(request);

        // BR-14: tên danh mục phải là duy nhất.
        if (jobCategoryRepository.existsByCategoryNameIgnoreCase(name)) {
            throw new BadRequestException("Tên danh mục đã tồn tại: " + name);
        }

        JobCategory category = new JobCategory();
        category.setCategoryName(name);
        JobCategory saved = jobCategoryRepository.save(category);

        auditLogService.record(currentAdmin(), "CREATE_CATEGORY", String.valueOf(saved.getId()), name);
        return JobCategoryResponse.from(saved);
    }

    @Override
    @Transactional
    public JobCategoryResponse updateJobCategory(Integer id, JobCategoryRequest request) {
        String name = requireName(request);

        JobCategory category = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục: " + id));

        // BR-14: tên mới không được trùng danh mục KHÁC. Giữ nguyên tên cũ thì vẫn hợp lệ.
        if (jobCategoryRepository.existsByCategoryNameIgnoreCaseAndIdNot(name, id)) {
            throw new BadRequestException("Tên danh mục đã tồn tại: " + name);
        }

        category.setCategoryName(name);
        JobCategory saved = jobCategoryRepository.save(category);

        auditLogService.record(currentAdmin(), "UPDATE_CATEGORY", String.valueOf(id), name);
        return JobCategoryResponse.from(saved);
    }

    @Override
    @Transactional
    public void deleteJobCategory(Integer id) {
        JobCategory category = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục: " + id));

        // BR-14: không được xoá danh mục còn tin tuyển dụng đang hoạt động trỏ vào, nếu không
        // các tin đó sẽ mất phân loại. Kiểm tra TRƯỚC khi xoá, không phải bắt lỗi khoá ngoại sau.
        if (jobPostRepository.existsByCategoryIdAndStatus(id, ACTIVE_JOB_POST_STATUS)) {
            throw new BadRequestException(
                    "Không thể xoá: danh mục \"" + category.getCategoryName()
                            + "\" đang được tin tuyển dụng sử dụng");
        }

        jobCategoryRepository.delete(category);
        auditLogService.record(currentAdmin(), "DELETE_CATEGORY", String.valueOf(id),
                category.getCategoryName());
    }

    private String requireName(JobCategoryRequest request) {
        if (request == null || request.categoryName() == null || request.categoryName().isBlank()) {
            throw new BadRequestException("Tên danh mục không được để trống");
        }
        return request.categoryName().trim();
    }

    // Xem chú thích cùng tên ở UserService: tạm dùng admin@ors.vn khi chưa có luồng đăng nhập.
    private User currentAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails details) {
            return details.getUser();
        }
        return userRepository.findUserByEmail(ADMIN_EMAIL_FALLBACK)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản admin"));
    }
}
