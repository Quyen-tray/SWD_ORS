package org.ors.subsystem.administration.user_management.service;

import org.ors.cross.Iam.security.user.CustomUserDetails;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.cross.share_kernel.exception.ResourceNotFoundException;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.ors.subsystem.administration.audit.AuditLogService;
import org.ors.subsystem.administration.user_management.dto.UserResponse;
import org.ors.subsystem.administration.user_management.state.AccountState;
import org.ors.subsystem.administration.user_management.state.AccountStates;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Nơi duy nhất BR-13 được thực thi. Controller không biết luật, repository không biết luật.
@Service
public class UserService implements IUserService {

    private static final String ADMIN_EMAIL_FALLBACK = "admin@ors.vn";

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public UserService(UserRepository userRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public List<UserResponse> getUsers(String keyword, String role, String status) {
        List<User> users;
        if (keyword != null && !keyword.isBlank()) {
            users = userRepository.findByKeyword(keyword.trim());
        } else if ((role != null && !role.isBlank()) || (status != null && !status.isBlank())) {
            users = userRepository.findByFilter(blankToNull(role), blankToNull(status));
        } else {
            users = userRepository.findAll();
        }
        return users.stream().map(UserResponse::from).toList();
    }

    @Override
    @Transactional
    public void activateUser(Integer userId) {
        // Kích hoạt lại không cần lý do: nó gỡ hạn chế chứ không áp đặt hạn chế.
        changeStatus(userId, "ACTIVATE_USER", null, AccountState::activate);
    }

    @Override
    @Transactional
    public void deactivateUser(Integer userId, String reason) {
        requireReason(reason);
        changeStatus(userId, "DEACTIVATE_USER", reason, AccountState::deactivate);
    }

    @Override
    @Transactional
    public void banUser(Integer userId, String reason) {
        requireReason(reason);
        changeStatus(userId, "BAN_USER", reason, AccountState::ban);
    }

    // Ba thao tác đổi trạng thái chỉ khác nhau ở nước đi yêu cầu state thực hiện, nên phần
    // còn lại (tìm user, chặn tự thao tác lên chính mình, lưu, ghi audit) gom vào một chỗ.
    private void changeStatus(Integer userId,
                              String actionType,
                              String reason,
                              java.util.function.Function<AccountState, AccountState> move) {
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + userId));

        User admin = currentAdmin();

        // BR-13: Admin không được tự tạm ngưng hoặc tự cấm tài khoản của chính mình.
        // Activate lên chính mình thì vô hại, nhưng chặn hết cho nhất quán và đơn giản.
        if (target.getId().equals(admin.getId())) {
            throw new BadRequestException("Admin không thể tự thao tác lên tài khoản của chính mình");
        }

        // State pattern: trạng thái hiện tại tự quyết định nước đi có hợp lệ không.
        // Nước đi sai sẽ ném BadRequestException ngay trong state, nên KHÔNG có
        // chuyển trạng thái sai nào được lưu xuống DB (NFR-FE07-1).
        AccountState next = move.apply(AccountStates.of(target.getStatus()));
        target.setStatus(next.status());
        userRepository.save(target);

        // BR-15: ghi vết sau khi ghi thành công, không phải trước.
        auditLogService.record(admin, actionType, String.valueOf(userId), reason);
    }

    private void requireReason(String reason) {
        // BR-13: tạm ngưng hoặc cấm thì bắt buộc phải ghi lý do để còn audit.
        if (reason == null || reason.isBlank()) {
            throw new BadRequestException("Phải nhập lý do");
        }
    }

    // Lấy Admin đang đăng nhập. Màn hình đăng nhập chưa được cài đặt (org.ors.cross.Iam.auth
    // vẫn rỗng) và SecurityConfig đang permitAll, nên khi chưa có token thì tạm lấy tài khoản
    // admin@ors.vn. Khi luồng đăng nhập xong thì nhánh fallback này bỏ đi, phần còn lại giữ nguyên.
    private User currentAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails details) {
            return details.getUser();
        }
        return userRepository.findUserByEmail(ADMIN_EMAIL_FALLBACK)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản admin"));
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
