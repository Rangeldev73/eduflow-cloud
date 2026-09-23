package dev.rangel.eduflow.enrollmentservice.infrastructure.messaging.producer;

import dev.rangel.eduflow.enrollmentservice.domain.model.OutboxEvent;
import dev.rangel.eduflow.enrollmentservice.infrastructure.persistence.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxPublisherService {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishEvent(UUID eventId) {
        OutboxEvent outboxEvent = outboxEventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("OutboxEvent not found for ID: " + eventId));

        if (outboxEvent.getPublishedAt() != null) {
            return;
        }

        byte[] body = outboxEvent.getPayload().getBytes(StandardCharsets.UTF_8);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding(StandardCharsets.UTF_8.name())
                .build();

        Message message = MessageBuilder.withBody(body).andProperties(properties).build();

        rabbitTemplate.send(outboxEvent.getExchangeName(), outboxEvent.getRoutingKey(), message);

        outboxEvent.markAsPublished();
        outboxEventRepository.save(outboxEvent);
    }
}