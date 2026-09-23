package dev.rangel.eduflow.enrollmentservice.infrastructure.messaging.producer;

import dev.rangel.eduflow.enrollmentservice.domain.model.OutboxEvent;
import dev.rangel.eduflow.enrollmentservice.infrastructure.persistence.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class OutboxPublisherScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxPublisherService outboxPublisherService;

    @Scheduled(fixedDelay = 5000)
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByPublishedAtIsNullOrderByCreatedAtAsc();

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Found {} pending outbox event(s) to publish.", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                outboxPublisherService.publishEvent(event.getId());
                log.info("Successfully published outbox event with ID: {}", event.getId());
            } catch (Exception e) {
                log.error("Failed to publish outbox event with ID: {}. Error: {}", event.getId(), e.getMessage(), e);
            }
        }
    }
}