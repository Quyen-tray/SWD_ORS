package org.ors.subsystem.moderation.job_moderation.notification;

import org.springframework.stereotype.Service;

// Ghép chain Decorator một lần duy nhất tại đây: Logging(Email(InApp(Base))).
// Phần còn lại của module chỉ biết gọi notifyRecruiter(...), không biết có bao nhiêu lớp
// decorator đang bọc bên trong.
@Service
public class NotificationService {

    private final Notifier notifier;

    public NotificationService() {
        this.notifier = new LoggingNotifierDecorator(
                new EmailNotifierDecorator(
                        new InAppNotifierDecorator(
                                new BaseNotifier())));
    }

    public void notifyRecruiter(String recipientEmail, String subject, String body) {
        notifier.notify(new NotificationContext(recipientEmail, subject, body));
    }
}
