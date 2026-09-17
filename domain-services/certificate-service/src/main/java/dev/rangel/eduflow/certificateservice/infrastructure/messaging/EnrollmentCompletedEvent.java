package dev.rangel.eduflow.certificateservice.infrastructure.messaging.event;

import java.util.UUID;

public record EnrollmentCompletedEvent(
        UUID enrollmentId,
        UUID studentId,
        UUID courseId
) {}