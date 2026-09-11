package dev.rangel.eduflow.enrollmentservice.application.dto.response;

import dev.rangel.eduflow.enrollmentservice.domain.model.Enrollment;
import dev.rangel.eduflow.enrollmentservice.domain.model.EnrollmentStatus;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentResponse(
        UUID id,
        UUID studentId,
        UUID courseId,
        EnrollmentStatus status,
        Instant createdAt,
        Long version
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getStatus(),
                enrollment.getCreatedAt(),
                enrollment.getVersion()
        );
    }
}