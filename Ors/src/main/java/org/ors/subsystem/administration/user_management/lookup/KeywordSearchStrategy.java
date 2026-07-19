package org.ors.subsystem.administration.user_management.lookup;

import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

// Nhánh 1 của UC-53: Admin gõ từ khoá vào ô tìm kiếm.
// Từ khoá được ưu tiên hơn bộ lọc, nên @Order thấp nhất (được hỏi trước).
@Order(1)
@Component
public class KeywordSearchStrategy implements UserLookupStrategy {

    private final UserRepository userRepository;

    public KeywordSearchStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(UserCriteria criteria) {
        return criteria.hasKeyword();
    }

    @Override
    public List<User> lookup(UserCriteria criteria) {
        return userRepository.findByKeyword(criteria.keyword());
    }
}
