package org.ors.subsystem.moderation.job_moderation.notification;

import lombok.extern.slf4j.Slf4j;

// Stub kênh email - chỉ log, không cấu hình SMTP thật cho phần demo này.
@Slf4j
public class EmailNotifierDecorator extends NotifierDecorator {

    public EmailNotifierDecorator(Notifier wrapped) {
        super(wrapped);
    }

    @Override
    public void notify(NotificationContext ctx) {
        super.notify(ctx);
        log.info("[EMAIL] to={} subject={} body={}", ctx.recipientEmail(), ctx.subject(), ctx.body());
    }
}
