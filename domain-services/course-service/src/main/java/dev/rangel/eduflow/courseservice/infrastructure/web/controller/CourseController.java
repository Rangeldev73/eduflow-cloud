package dev.rangel.eduflow.courseservice.infrastructure.web.controller;

import dev.rangel.eduflow.courseservice.application.dto.request.AddModuleRequest;
import dev.rangel.eduflow.courseservice.application.dto.request.CreateCourseRequest;
import dev.rangel.eduflow.courseservice.application.dto.response.CourseResponse;
import dev.rangel.eduflow.courseservice.application.usecase.AddModuleUseCase;
import dev.rangel.eduflow.courseservice.application.usecase.CreateCourseUseCase;
import dev.rangel.eduflow.courseservice.application.usecase.GetCourseByIdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CreateCourseUseCase createCourseUseCase;
    private final AddModuleUseCase addModuleUseCase;
    private final GetCourseByIdUseCase getCourseByIdUseCase;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody @Valid CreateCourseRequest request) {
        CourseResponse response = createCourseUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/modules")
    public ResponseEntity<CourseResponse> addModule(
            @PathVariable UUID id,
            @RequestBody @Valid AddModuleRequest request
    ) {
        CourseResponse response = addModuleUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable UUID id) {
        CourseResponse response = getCourseByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}