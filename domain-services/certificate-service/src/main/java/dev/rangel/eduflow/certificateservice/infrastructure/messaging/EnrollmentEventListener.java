package dev.rangel.eduflow.certificateservice.infrastructure.messaging;

import dev.rangel.eduflow.certificateservice.domain.model.Certificate;
import dev.rangel.eduflow.certificateservice.infrastructure.persistence.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentEventListener {

    private final CertificateRepository certificateRepository;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "certificate.enrollment-completed.queue", durable = "true"),
            exchange = @Exchange(value = "enrollment.events", type = "topic"),
            key = "enrollment.completed"
    ))
    public void handleEnrollmentCompleted(EnrollmentCompletedEvent event) {
        log.info("Received EnrollmentCompleted event for enrollment ID: {}", event.enrollmentId());

        try {
            Certificate certificate = Certificate.issue(
                    event.enrollmentId(),
                    event.studentId(),
                    event.courseId()
            );

            certificateRepository.save(certificate);
            log.info("Certificate successfully issued with code: {} for enrollment ID: {}",
                    certificate.getCertificateCode(), event.enrollmentId());

        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate processing detected for enrollment ID: {}. Event already processed. Acknowledging message.",
                    event.enrollmentId());
        }
    }
}