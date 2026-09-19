package dev.rangel.eduflow.enrollmentservice.domain.exception;

import java.util.UUID;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(UUID courseId) {
        super(String.format("Course with ID '%s' was not found.", courseId));
    }

    public CourseNotFoundException(String message) {
        super(message);
    }
}