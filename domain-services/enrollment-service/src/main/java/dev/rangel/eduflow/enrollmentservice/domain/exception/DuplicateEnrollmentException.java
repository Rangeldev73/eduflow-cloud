package dev.rangel.eduflow.enrollmentservice.domain.exception;

import java.util.UUID;

public class DuplicateEnrollmentException extends RuntimeException {

    public DuplicateEnrollmentException(UUID studentId, UUID courseId) {
        super(String.format("Student with ID '%s' is already enrolled in course '%s'.", studentId, courseId));
    }

    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}