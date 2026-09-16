package dev.rangel.eduflow.enrollmentservice.application.usecase;

import dev.rangel.eduflow.enrollmentservice.application.dto.response.EnrollmentResponse;
import dev.rangel.eduflow.enrollmentservice.application.event.EnrollmentCompletedEvent;
import dev.rangel.eduflow.enrollmentservice.domain.exception.EnrollmentNotFoundException;
import dev.rangel.eduflow.enrollmentservice.domain.model.Enrollment;
import dev.rangel.eduflow.enrollmentservice.infrastructure.messaging.producer.EnrollmentEventProducer;
import dev.rangel.eduflow.enrollmentservice.infrastructure.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompleteEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentEventProducer eventProducer;

    @Transactional
    public EnrollmentResponse execute(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));

        enrollment.complete();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        EnrollmentCompletedEvent event = new EnrollmentCompletedEvent(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                Instant.now()
        );
        eventProducer.sendEnrollmentCompleted(event);

        return EnrollmentResponse.from(savedEnrollment);
    }
}
