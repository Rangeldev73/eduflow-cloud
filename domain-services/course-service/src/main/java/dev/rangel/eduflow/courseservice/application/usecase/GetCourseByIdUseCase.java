package dev.rangel.eduflow.courseservice.application.usecase;

import dev.rangel.eduflow.courseservice.application.dto.response.CourseResponse;
import dev.rangel.eduflow.courseservice.domain.exception.CourseNotFoundException;
import dev.rangel.eduflow.courseservice.domain.model.Course;
import dev.rangel.eduflow.courseservice.infrastructure.persistence.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetCourseByIdUseCase {

    private final CourseRepository courseRepository;

    public CourseResponse execute(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        return CourseResponse.from(course);
    }
}