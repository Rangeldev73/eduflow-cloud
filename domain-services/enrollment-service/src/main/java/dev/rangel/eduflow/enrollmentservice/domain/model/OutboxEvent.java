package dev.rangel.eduflow.enrollmentservice.domain.model;

import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_outbox_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "exchange_name", nullable = false)
    private String exchangeName;

    @Column(name = "routing_key", nullable = false)
    private String routingKey;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    private OutboxEvent(String aggregateType, UUID aggregateId, String eventType,
                        String exchangeName, String routingKey, String payload) {
        validateInputs(aggregateType, aggregateId, eventType, exchangeName, routingKey, payload);
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.payload = payload;
        this.createdAt = Instant.now();
        this.publishedAt = null;
    }

    public static OutboxEvent create(String aggregateType, UUID aggregateId, String eventType,
                                     String exchangeName, String routingKey, String payload) {
        return new OutboxEvent(aggregateType, aggregateId, eventType, exchangeName, routingKey, payload);
    }

    public void markAsPublished() {
        if (this.publishedAt != null) {
            throw new IllegalStateException("Event has already been published");
        }
        this.publishedAt = Instant.now();
    }

    private void validateInputs(String aggregateType, UUID aggregateId, String eventType,
                                String exchangeName, String routingKey, String payload) {
        if (aggregateType == null || aggregateType.isBlank()) {
            throw new IllegalArgumentException("Aggregate type cannot be null or empty");
        }
        if (aggregateId == null) {
            throw new IllegalArgumentException("Aggregate ID cannot be null");
        }
        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        if (exchangeName == null || exchangeName.isBlank()) {
            throw new IllegalArgumentException("Exchange name cannot be null or empty");
        }
        if (routingKey == null || routingKey.isBlank()) {
            throw new IllegalArgumentException("Routing key cannot be null or empty");
        }
        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("Payload cannot be null or empty");
        }
    }
}
