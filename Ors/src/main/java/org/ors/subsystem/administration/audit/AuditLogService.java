package org.ors.subsystem.administration.audit;

import org.ors.cross.share_kernel.entity.AuditLog;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

// BR-15 / NFR-FE07-2: mọi thao tác quản trị đều phải để lại vết.
// Bảng audit_logs chỉ có 4 cột (user_id = người thực hiện, action_type, description,
// created_at), không có cột riêng cho đối tượng bị tác động và lý do. Vì vậy target và
// reason được ghi vào description theo một định dạng cố định "target=<id>; reason=<text>"
// để còn tra lại được. (Đề xuất thêm 2 cột target_user_id + reason đã gửi cho nhóm.)
@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void record(User actor, String actionType, String target, String reason) {
        AuditLog log = new AuditLog();
        log.setUser(actor);
        log.setActionType(actionType);
        log.setDescription("target=" + target + (reason == null ? "" : "; reason=" + reason));
        auditLogRepository.save(log);
    }
}
