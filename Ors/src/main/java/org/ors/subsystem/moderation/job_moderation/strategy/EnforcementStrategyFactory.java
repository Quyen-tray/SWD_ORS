package org.ors.subsystem.moderation.job_moderation.strategy;

import org.ors.cross.share_kernel.exception.BadRequestException;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Spring tự tiêm CẢ DANH SÁCH bean cài đặt EnforcementStrategy vào constructor (cùng cách
// UserLookupSelector bên module Admin nhận List<UserLookupStrategy>). Thêm strategy mới chỉ
// cần thêm @Component mới - KHÔNG phải sửa file này.
//
// Khác UserLookupSelector: lựa chọn ở đây là theo type tường minh do Moderator chọn (không
// phải "chiến lược nào nhận tiêu chí này trước"), nên dùng Map tra thẳng theo key thay vì
// supports()/@Order.
@Component
public class EnforcementStrategyFactory {

    private final Map<EnforcementType, EnforcementStrategy> strategies;

    public EnforcementStrategyFactory(List<EnforcementStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(EnforcementStrategy::type, Function.identity()));
    }

    public EnforcementStrategy resolve(EnforcementType type) {
        EnforcementStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new BadRequestException("Không hỗ trợ hành động xử lý: " + type);
        }
        return strategy;
    }
}
