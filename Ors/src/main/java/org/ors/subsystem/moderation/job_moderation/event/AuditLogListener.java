package org.ors.subsystem.moderation.job_moderation.event;

import org.ors.cross.share_kernel.entity.AuditLog;
import org.ors.cross.share_kernel.repository.AuditLogRepository;
import org.ors.cross.share_kernel.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

// BR-10: MỌI hành động của Moderator phải ghi audit_logs, không rải rác if/else trong
// từng service. Ghi thẳng qua AuditLogRepository (share kernel) - KHÔNG dùng
// administration.audit.AuditLogService vì đó là code của module khác (Admin), phụ thuộc
// chéo module sẽ làm 2 phần dính vào nhau không cần thiết.
@Component
public class AuditLogListener {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogListener(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    public void onEvent(ModerationEvent event) {
        AuditLog log = new AuditLog();
        log.setUser(userRepository.getReferenceById(event.moderatorId()));
        log.setActionType(event.actionType());
        log.setDescription(AuditDescriptionCodec.encode(event.entityType(), event.entityId(), event.description()));
        // @ColumnDefault("getdate()") chỉ áp dụng khi Hibernate KHÔNG gửi giá trị nào cho
        // cột này - nhưng Hibernate luôn gửi created_at=NULL tường minh cho field chưa set,
        // nên phải tự set ở đây, không thể trông chờ default phía DB.
        log.setCreatedAt(Instant.now());
        auditLogRepository.save(log);
    }
}
