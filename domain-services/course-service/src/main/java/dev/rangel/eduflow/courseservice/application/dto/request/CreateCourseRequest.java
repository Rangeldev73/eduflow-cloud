package dev.rangel.eduflow.courseservice.application.dto.request;

import dev.rangel.eduflow.courseservice.domain.model.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCourseRequest(
        @NotBlank(message = "Title is mandatory")
        @Size(max = 255)
        String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @NotNull(message = "Level is mandatory")
        CourseLevel level
) {}