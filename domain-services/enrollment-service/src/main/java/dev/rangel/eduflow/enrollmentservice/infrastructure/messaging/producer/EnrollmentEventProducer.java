package dev.rangel.eduflow.enrollmentservice.infrastructure.messaging.producer;

import dev.rangel.eduflow.enrollmentservice.application.event.EnrollmentCompletedEvent;
import dev.rangel.eduflow.enrollmentservice.application.event.EnrollmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import static dev.rangel.eduflow.enrollmentservice.infrastructure.messaging.config.RabbitMQConfig.EXCHANGE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEnrollmentCreated(EnrollmentCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "enrollment.created", event);
        } catch (Exception e) {
            log.error("Failed to publish EnrollmentCreatedEvent for enrollment ID: {}. Error: {}",
                    event.enrollmentId(), e.getMessage(), e);
        }
    }

    public void sendEnrollmentCompleted(EnrollmentCompletedEvent event) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "enrollment.completed", event);
        } catch (Exception e) {
            log.error("Failed to publish EnrollmentCompletedEvent for enrollment ID: {}. Error: {}",
                    event.enrollmentId(), e.getMessage(), e);
        }
    }
}