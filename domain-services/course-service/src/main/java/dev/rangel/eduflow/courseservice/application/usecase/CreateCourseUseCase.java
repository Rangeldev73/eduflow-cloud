package dev.rangel.eduflow.courseservice.application.usecase;

import dev.rangel.eduflow.courseservice.application.dto.request.CreateCourseRequest;
import dev.rangel.eduflow.courseservice.application.dto.response.CourseResponse;
import dev.rangel.eduflow.courseservice.domain.model.Course;
import dev.rangel.eduflow.courseservice.infrastructure.persistence.repository.CourseRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCourseUseCase {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseResponse execute(CreateCourseRequest dto) {

        Course course = new Course(dto.title(), dto.description(), dto.level());
        Course savedCourse = courseRepository.save(course);

        return CourseResponse.from(savedCourse);
    }
}