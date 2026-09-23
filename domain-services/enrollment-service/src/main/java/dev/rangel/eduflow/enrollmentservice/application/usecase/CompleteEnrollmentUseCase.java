package dev.rangel.eduflow.enrollmentservice.application.usecase;

import dev.rangel.eduflow.enrollmentservice.application.dto.response.EnrollmentResponse;
import dev.rangel.eduflow.enrollmentservice.application.event.EnrollmentCompletedEvent;
import dev.rangel.eduflow.enrollmentservice.domain.exception.EnrollmentNotFoundException;
import dev.rangel.eduflow.enrollmentservice.domain.model.Enrollment;
import dev.rangel.eduflow.enrollmentservice.domain.model.OutboxEvent;
import dev.rangel.eduflow.enrollmentservice.infrastructure.messaging.config.RabbitMQConfig;
import dev.rangel.eduflow.enrollmentservice.infrastructure.persistence.repository.EnrollmentRepository;
import dev.rangel.eduflow.enrollmentservice.infrastructure.persistence.repository.OutboxEventRepository;
import dev.rangel.eduflow.enrollmentservice.infrastructure.serializer.EventSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompleteEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final EventSerializer eventSerializer;

    @Transactional
    public EnrollmentResponse execute(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));

        enrollment.complete();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        EnrollmentCompletedEvent event = new EnrollmentCompletedEvent(
                savedEnrollment.getId(),
                savedEnrollment.getStudentId(),
                savedEnrollment.getCourseId(),
                Instant.now()
        );

        String payload = eventSerializer.serialize(event);

        OutboxEvent outboxEvent = OutboxEvent.create(
                "Enrollment",
                savedEnrollment.getId(),
                "EnrollmentCompleted",
                RabbitMQConfig.EXCHANGE_NAME,
                "enrollment.completed",
                payload
        );

        outboxEventRepository.save(outboxEvent);

        return EnrollmentResponse.from(savedEnrollment);
    }
}