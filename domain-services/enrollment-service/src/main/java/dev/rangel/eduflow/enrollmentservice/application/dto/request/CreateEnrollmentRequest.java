package dev.rangel.eduflow.enrollmentservice.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateEnrollmentRequest(
        @NotNull(message = "Student ID cannot be null")
        UUID studentId,

        @NotNull(message = "Course ID cannot be null")
        UUID courseId
) {}