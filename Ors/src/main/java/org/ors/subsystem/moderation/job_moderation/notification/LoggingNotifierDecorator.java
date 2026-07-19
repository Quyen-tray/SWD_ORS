package org.ors.subsystem.moderation.job_moderation.notification;

import lombok.extern.slf4j.Slf4j;

// Lớp ngoài cùng của chain - ghi lại việc dispatch đã chạy qua các kênh bên trong,
// tách biệt với audit_logs (BR-10) vốn ghi hành động nghiệp vụ chứ không phải log kỹ thuật.
@Slf4j
public class LoggingNotifierDecorator extends NotifierDecorator {

    public LoggingNotifierDecorator(Notifier wrapped) {
        super(wrapped);
    }

    @Override
    public void notify(NotificationContext ctx) {
        super.notify(ctx);
        log.debug("Notification dispatch completed for {}", ctx.recipientEmail());
    }
}
