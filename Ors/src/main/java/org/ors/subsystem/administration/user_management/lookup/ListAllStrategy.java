package org.ors.subsystem.administration.user_management.lookup;

import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

// Nhánh 3 của UC-53: Admin mở màn hình mà chưa nhập tiêu chí nào.
// Đây là chiến lược mặc định: nhận mọi tiêu chí, nên @Order cao nhất để được hỏi sau
// cùng. Nhờ có nó mà bộ chọn (UserLookupSelector) luôn tìm được một chiến lược, không
// bao giờ rơi vào trường hợp "không ai nhận việc".
@Order(Integer.MAX_VALUE)
@Component
public class ListAllStrategy implements UserLookupStrategy {

    private final UserRepository userRepository;

    public ListAllStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(UserCriteria criteria) {
        return true;
    }

    @Override
    public List<User> lookup(UserCriteria criteria) {
        return userRepository.findAll();
    }
}
