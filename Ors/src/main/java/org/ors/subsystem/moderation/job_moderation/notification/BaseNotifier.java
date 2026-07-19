package org.ors.subsystem.moderation.job_moderation.notification;

// ConcreteComponent - đáy của chain, không làm gì cả. Mọi kênh gửi thật nằm ở decorator.
public class BaseNotifier implements Notifier {
    @Override
    public void notify(NotificationContext ctx) {
        // no-op
    }
}
