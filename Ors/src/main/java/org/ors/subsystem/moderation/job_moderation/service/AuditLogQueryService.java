package org.ors.subsystem.moderation.job_moderation.service;

import org.ors.cross.share_kernel.entity.AuditLog;
import org.ors.cross.share_kernel.repository.AuditLogRepository;
import org.ors.subsystem.moderation.job_moderation.dto.AuditLogResponse;
import org.ors.subsystem.moderation.job_moderation.enums.EntityType;
import org.ors.subsystem.moderation.job_moderation.event.AuditDescriptionCodec;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEvent;
import org.ors.subsystem.moderation.job_moderation.event.ModerationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

// UC-50 - đọc + lọc, không có ghi. Lọc in-memory (dữ liệu demo nhỏ) thay vì query SQL phức
// tạp, vì entityType/entityId không phải cột thật mà được giải mã từ description.
@Service
public class AuditLogQueryService implements IAuditLogQueryService {

    private final AuditLogRepository auditLogRepository;
    private final ModerationEventPublisher eventPublisher;

    public AuditLogQueryService(AuditLogRepository auditLogRepository, ModerationEventPublisher eventPublisher) {
        this.auditLogRepository = auditLogRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<AuditLogResponse> query(Integer moderatorId, String entityType, Integer entityId,
                                         String actionType, Instant from, Instant to,
                                         boolean mineOnly, Integer requestingModeratorId) {
        Integer effectiveModeratorId = mineOnly ? requestingModeratorId : moderatorId;

        List<AuditLogResponse> result = auditLogRepository.findAll().stream()
                .map(this::toResponse)
                .filter(a -> effectiveModeratorId == null || effectiveModeratorId.equals(a.moderatorId()))
                .filter(a -> entityType == null || entityType.equalsIgnoreCase(a.entityType()))
                .filter(a -> entityId == null || entityId.equals(a.entityId()))
                .filter(a -> actionType == null || actionType.equalsIgnoreCase(a.actionType()))
                .filter(a -> from == null || (a.createdAt() != null && !a.createdAt().isBefore(from)))
                .filter(a -> to == null || (a.createdAt() != null && !a.createdAt().isAfter(to)))
                .sorted(Comparator.comparing(AuditLogResponse::createdAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();

        eventPublisher.publish(ModerationEvent.auditOnly(requestingModeratorId, "AUDIT_LOG_ACCESS",
                EntityType.REPORT, null, "Moderator queried audit logs"));
        return result;
    }

    private AuditLogResponse toResponse(AuditLog log) {
        AuditDescriptionCodec.Decoded decoded = AuditDescriptionCodec.decode(log.getDescription());
        return new AuditLogResponse(log.getId(), log.getCreatedAt(), log.getUser().getId(),
                log.getActionType(), decoded.entityType(), decoded.entityId(), decoded.message());
    }
}
