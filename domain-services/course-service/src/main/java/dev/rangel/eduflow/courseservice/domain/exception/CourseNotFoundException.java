package dev.rangel.eduflow.courseservice.domain.exception;

import java.util.UUID;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(UUID courseId) {
        super(String.format("Course with ID '%s' was not found.", courseId));
    }

    public CourseNotFoundException(String message) {
        super(message);
    }
}