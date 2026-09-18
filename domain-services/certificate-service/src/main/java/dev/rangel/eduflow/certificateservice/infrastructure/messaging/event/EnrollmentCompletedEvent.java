package dev.rangel.eduflow.certificateservice.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentCompletedEvent(
        UUID enrollmentId,
        UUID studentId,
        UUID courseId,
        Instant completedAt
) {}