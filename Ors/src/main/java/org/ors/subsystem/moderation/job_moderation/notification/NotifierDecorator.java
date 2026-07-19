package org.ors.subsystem.moderation.job_moderation.notification;

// Decorator trừu tượng: bọc quanh 1 Notifier khác và luôn gọi tiếp xuống dưới trước
// khi làm phần việc riêng của mình. Con cụ thể chỉ cần override notify() và gọi
// super.notify(ctx) trước.
public abstract class NotifierDecorator implements Notifier {

    protected final Notifier wrapped;

    protected NotifierDecorator(Notifier wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void notify(NotificationContext ctx) {
        wrapped.notify(ctx);
    }
}
