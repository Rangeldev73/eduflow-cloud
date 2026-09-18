package dev.rangel.eduflow.notificationservice.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentCreatedEvent(
        UUID enrollmentId,
        UUID studentId,
        UUID courseId,
        Instant createdAt
) {}