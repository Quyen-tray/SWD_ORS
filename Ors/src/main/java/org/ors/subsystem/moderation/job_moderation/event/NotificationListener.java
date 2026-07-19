package org.ors.subsystem.moderation.job_moderation.event;

import org.ors.subsystem.moderation.job_moderation.notification.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// Observer thứ 2 lắng nghe cùng ModerationEvent. Không phải hành động nào cũng cần báo
// Recruiter (vd truy cập dashboard thì không) - notificationSubject == null là tín hiệu bỏ qua.
@Component
public class NotificationListener {

    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onEvent(ModerationEvent event) {
        if (event.notificationSubject() != null) {
            notificationService.notifyRecruiter(event.recipientEmail(), event.notificationSubject(), event.notificationBody());
        }
    }
}
