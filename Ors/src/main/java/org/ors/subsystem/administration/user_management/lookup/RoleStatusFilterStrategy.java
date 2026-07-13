package org.ors.subsystem.administration.user_management.lookup;

import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

// Nhánh 2 của UC-53: Admin không gõ từ khoá mà chọn vai trò và/hoặc trạng thái.
// Chỉ nhận việc khi không có từ khoá, nên @Order sau KeywordSearchStrategy.
@Order(2)
@Component
public class RoleStatusFilterStrategy implements UserLookupStrategy {

    private final UserRepository userRepository;

    public RoleStatusFilterStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(UserCriteria criteria) {
        return !criteria.hasKeyword() && criteria.hasRoleOrStatus();
    }

    @Override
    public List<User> lookup(UserCriteria criteria) {
        // Truyền null cho tiêu chí không chọn: câu query bỏ qua vế đó.
        return userRepository.findByFilter(criteria.role(), criteria.status());
    }
}
