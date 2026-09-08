package dev.rangel.eduflow.courseservice.application.dto.response;

import dev.rangel.eduflow.courseservice.domain.model.Course;
import dev.rangel.eduflow.courseservice.domain.model.CourseLevel;
import java.util.List;
import java.util.UUID;

public record CourseResponse(
        UUID id,
        String title,
        String description,
        CourseLevel level,
        int totalWorkload,
        Long version,
        List<ModuleResponse> modules
) {
    public static CourseResponse from(Course course) {
        List<ModuleResponse> moduleResponses = course.getModules().stream()
                .map(m -> new ModuleResponse(
                        m.getId(),
                        m.getTitle(),
                        m.getDescription(),
                        m.getModuleOrder(),
                        m.getDuration()
                ))
                .toList();

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getTotalWorkload(),
                course.getVersion(),
                moduleResponses
        );
    }

    public record ModuleResponse(
            UUID id,
            String title,
            String description,
            int moduleOrder,
            int duration
    ){}
}