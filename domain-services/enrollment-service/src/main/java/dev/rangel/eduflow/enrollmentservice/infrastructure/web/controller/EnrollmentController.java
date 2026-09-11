package dev.rangel.eduflow.enrollmentservice.infrastructure.web.controller;

import dev.rangel.eduflow.enrollmentservice.application.dto.request.CreateEnrollmentRequest;
import dev.rangel.eduflow.enrollmentservice.application.dto.response.EnrollmentResponse;
import dev.rangel.eduflow.enrollmentservice.application.usecase.CancelEnrollmentUseCase;
import dev.rangel.eduflow.enrollmentservice.application.usecase.CompleteEnrollmentUseCase;
import dev.rangel.eduflow.enrollmentservice.application.usecase.CreateEnrollmentUseCase;
import dev.rangel.eduflow.enrollmentservice.application.usecase.GetEnrollmentByIdUseCase;
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
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final CreateEnrollmentUseCase createEnrollmentUseCase;
    private final CompleteEnrollmentUseCase completeEnrollmentUseCase;
    private final CancelEnrollmentUseCase cancelEnrollmentUseCase;
    private final GetEnrollmentByIdUseCase getEnrollmentByIdUseCase;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> createEnrollment(@RequestBody @Valid CreateEnrollmentRequest request) {
        EnrollmentResponse response = createEnrollmentUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<EnrollmentResponse> completeEnrollment(@PathVariable("id") UUID id) {
        EnrollmentResponse response = completeEnrollmentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<EnrollmentResponse> cancelEnrollment(@PathVariable("id") UUID id) {
        EnrollmentResponse response = cancelEnrollmentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable("id") UUID id) {
        EnrollmentResponse response = getEnrollmentByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}