package org.ors.subsystem.moderation.job_moderation.notification;

public record NotificationContext(String recipientEmail, String subject, String body) {
}
