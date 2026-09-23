package dev.rangel.eduflow.enrollmentservice.domain.exception;

public class EventSerializationException extends RuntimeException {
    public EventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}