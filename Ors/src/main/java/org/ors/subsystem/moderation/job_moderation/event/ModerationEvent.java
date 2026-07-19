package org.ors.subsystem.moderation.job_moderation.event;

import org.ors.subsystem.moderation.job_moderation.enums.EntityType;

// Observer pattern (BR-10): mọi hành động kiểm duyệt publish đúng một event này.
// AuditLogListener luôn ghi audit; NotificationListener chỉ báo Recruiter khi
// notificationSubject != null (không phải hành động nào cũng cần thông báo).
public record ModerationEvent(
        Integer moderatorId,
        String actionType,
        EntityType entityType,
        Integer entityId,
        String description,
        String recipientEmail,
        String notificationSubject,
        String notificationBody
) {
    public static ModerationEvent auditOnly(Integer moderatorId, String actionType,
                                             EntityType entityType, Integer entityId,
                                             String description) {
        return new ModerationEvent(moderatorId, actionType, entityType, entityId, description,
                null, null, null);
    }

    public static ModerationEvent withNotification(Integer moderatorId, String actionType,
                                                    EntityType entityType, Integer entityId,
                                                    String description, String recipientEmail,
                                                    String notificationSubject, String notificationBody) {
        return new ModerationEvent(moderatorId, actionType, entityType, entityId, description,
                recipientEmail, notificationSubject, notificationBody);
    }
}
