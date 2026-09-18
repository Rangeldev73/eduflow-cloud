package dev.rangel.eduflow.notificationservice.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record CertificateIssuedEvent(
        UUID certificateId,
        UUID enrollmentId,
        UUID studentId,
        UUID courseId,
        String certificateCode,
        Instant issuedAt
) {}