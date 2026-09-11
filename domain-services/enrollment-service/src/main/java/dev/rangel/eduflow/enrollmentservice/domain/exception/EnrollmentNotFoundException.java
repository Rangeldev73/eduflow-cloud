package dev.rangel.eduflow.enrollmentservice.domain.exception;

import java.util.UUID;

public class EnrollmentNotFoundException extends RuntimeException {

    public EnrollmentNotFoundException(UUID enrollmentId) {
        super(String.format("Enrollment with ID '%s' was not found.", enrollmentId));
    }
    public EnrollmentNotFoundException(String message) {
        super(message);
    }
}