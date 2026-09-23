package dev.rangel.eduflow.enrollmentservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OutboxEventTest {

    private final String aggregateType = "Enrollment";
    private final UUID aggregateId = UUID.randomUUID();
    private final String eventType = "EnrollmentCreated";
    private final String exchangeName = "eduflow.events";
    private final String routingKey = "enrollment.created";
    private final String payload = "{\"id\":\"123\"}";

    @Test
    @DisplayName("Should create OutboxEvent successfully with null publishedAt")
    void shouldCreateOutboxEventSuccessfully() {
        OutboxEvent event = OutboxEvent.create(
                aggregateType, aggregateId, eventType, exchangeName, routingKey, payload
        );

        assertEquals(aggregateType, event.getAggregateType());
        assertEquals(aggregateId, event.getAggregateId());
        assertEquals(eventType, event.getEventType());
        assertEquals(exchangeName, event.getExchangeName());
        assertEquals(routingKey, event.getRoutingKey());
        assertEquals(payload, event.getPayload());
        assertNotNull(event.getCreatedAt());
        assertNull(event.getPublishedAt());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputs")
    @DisplayName("Should throw IllegalArgumentException when creation arguments are invalid")
    void shouldThrowExceptionWhenArgumentsAreInvalid(
            String aggType, UUID aggId, String evtType, String exchange, String rKey, String body) {

        assertThrows(IllegalArgumentException.class, () ->
                OutboxEvent.create(aggType, aggId, evtType, exchange, rKey, body)
        );
    }

    private static Stream<Arguments> provideInvalidInputs() {
        UUID validId = UUID.randomUUID();
        return Stream.of(
                Arguments.of(null, validId, "Event", "exchange", "key", "{}"),
                Arguments.of("", validId, "Event", "exchange", "key", "{}"),
                Arguments.of("  ", validId, "Event", "exchange", "key", "{}"),
                Arguments.of("Aggregate", null, "Event", "exchange", "key", "{}"),
                Arguments.of("Aggregate", validId, null, "exchange", "key", "{}"),
                Arguments.of("Aggregate", validId, "", "exchange", "key", "{}"),
                Arguments.of("Aggregate", validId, "Event", null, "key", "{}"),
                Arguments.of("Aggregate", validId, "Event", "", "key", "{}"),
                Arguments.of("Aggregate", validId, "Event", "exchange", null, "{}"),
                Arguments.of("Aggregate", validId, "Event", "exchange", "", "{}"),
                Arguments.of("Aggregate", validId, "Event", "exchange", "key", null),
                Arguments.of("Aggregate", validId, "Event", "exchange", "key", "")
        );
    }

    @Test
    @DisplayName("Should mark event as published successfully")
    void shouldMarkAsPublishedSuccessfully() {
        OutboxEvent event = OutboxEvent.create(
                aggregateType, aggregateId, eventType, exchangeName, routingKey, payload
        );

        event.markAsPublished();

        assertNotNull(event.getPublishedAt());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when marking an already published event")
    void shouldThrowExceptionWhenMarkingAlreadyPublishedEvent() {
        OutboxEvent event = OutboxEvent.create(
                aggregateType, aggregateId, eventType, exchangeName, routingKey, payload
        );
        event.markAsPublished();

        assertThrows(IllegalStateException.class, event::markAsPublished);
    }
}