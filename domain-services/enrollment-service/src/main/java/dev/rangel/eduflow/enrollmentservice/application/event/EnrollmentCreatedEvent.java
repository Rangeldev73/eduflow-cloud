package dev.rangel.eduflow.enrollmentservice.application.event;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentCreatedEvent(
        UUID enrollmentId,
        UUID studentId,
        UUID courseId,
        Instant createdAt
) {}