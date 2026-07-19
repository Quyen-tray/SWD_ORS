package org.ors.subsystem.moderation.job_moderation.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

// Bọc ApplicationEventPublisher của Spring để service layer chỉ phụ thuộc vào 1 method
// publish(event), không phụ thuộc trực tiếp vào Spring event API.
@Component
public class ModerationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ModerationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(ModerationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
