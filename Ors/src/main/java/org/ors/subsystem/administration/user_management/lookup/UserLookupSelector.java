package org.ors.subsystem.administration.user_management.lookup;

import org.ors.cross.share_kernel.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

// Chọn chiến lược tra cứu ứng với tiêu chí Admin nhập vào.
//
// Spring tự tiêm CẢ DANH SÁCH các bean cài đặt UserLookupStrategy vào constructor, và
// xếp sẵn theo @Order. Vì vậy thêm một cách tra cứu mới (vd lọc theo khoảng ngày tạo)
// chỉ cần viết thêm một class @Component implements UserLookupStrategy - KHÔNG phải sửa
// file này, cũng không phải sửa UserService.
//
// ListAllStrategy nhận mọi tiêu chí và đứng cuối danh sách, nên luôn có người nhận việc.
@Component
public class UserLookupSelector {

    private final List<UserLookupStrategy> strategies;

    public UserLookupSelector(List<UserLookupStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<User> lookup(UserCriteria criteria) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(criteria))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Không có chiến lược tra cứu nào nhận tiêu chí này"))
                .lookup(criteria);
    }
}
