package dev.rangel.eduflow.enrollmentservice.application.event;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentCompletedEvent(
        UUID enrollmentId,
        UUID studentId,
        UUID courseId,
        Instant completedAt
) {}