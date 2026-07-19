package org.ors.subsystem.moderation.job_moderation.notification;

import lombok.extern.slf4j.Slf4j;

// Stub kênh in-app - chỉ log.
@Slf4j
public class InAppNotifierDecorator extends NotifierDecorator {

    public InAppNotifierDecorator(Notifier wrapped) {
        super(wrapped);
    }

    @Override
    public void notify(NotificationContext ctx) {
        super.notify(ctx);
        log.info("[IN-APP] to={} subject={}", ctx.recipientEmail(), ctx.subject());
    }
}
