package dev.rangel.eduflow.courseservice.application.usecase;

import dev.rangel.eduflow.courseservice.application.dto.request.AddModuleRequest;
import dev.rangel.eduflow.courseservice.application.dto.response.CourseResponse;
import dev.rangel.eduflow.courseservice.domain.exception.CourseNotFoundException;
import dev.rangel.eduflow.courseservice.domain.model.Course;
import dev.rangel.eduflow.courseservice.infrastructure.persistence.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddModuleUseCase {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseResponse execute(UUID courseId, AddModuleRequest dto) {

        Course course = courseRepository.findByIdWithOptimisticLock(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        course.addModule(dto.title(), dto.description(), dto.duration());
        Course savedCourse = courseRepository.save(course);

        return CourseResponse.from(savedCourse);
    }
}