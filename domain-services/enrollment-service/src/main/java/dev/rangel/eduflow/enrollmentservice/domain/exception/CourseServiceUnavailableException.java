package dev.rangel.eduflow.enrollmentservice.domain.exception;

public class CourseServiceUnavailableException extends RuntimeException {
    public CourseServiceUnavailableException(String message) {
        super(message);
    }
}