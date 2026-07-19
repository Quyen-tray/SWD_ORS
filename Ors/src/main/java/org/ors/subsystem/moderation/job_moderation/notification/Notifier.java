package org.ors.subsystem.moderation.job_moderation.notification;

// Decorator pattern - Component. Chain được ghép trong NotificationService.
public interface Notifier {
    void notify(NotificationContext ctx);
}
