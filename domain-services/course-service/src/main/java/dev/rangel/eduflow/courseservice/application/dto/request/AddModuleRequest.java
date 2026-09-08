package dev.rangel.eduflow.courseservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddModuleRequest(
        @NotBlank(message = "Title is mandatory")
        @Size(max = 255)
        String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Positive(message = "Duration must be greater than zero")
        int duration
) {}