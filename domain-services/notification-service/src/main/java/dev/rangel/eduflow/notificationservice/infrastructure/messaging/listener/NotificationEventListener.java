package dev.rangel.eduflow.notificationservice.infrastructure.messaging.listener;

import dev.rangel.eduflow.notificationservice.infrastructure.messaging.event.CertificateIssuedEvent;
import dev.rangel.eduflow.notificationservice.infrastructure.messaging.event.EnrollmentCompletedEvent;
import dev.rangel.eduflow.notificationservice.infrastructure.messaging.event.EnrollmentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationEventListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "notification.enrollment-created.queue", durable = "true"),
            exchange = @Exchange(value = "enrollment.events", type = "topic"),
            key = "enrollment.created"
    ))
    public void handleEnrollmentCreated(EnrollmentCreatedEvent event) {
        log.info("[NOTIFICATION] Welcome! Student {} successfully enrolled in course {}. (Enrollment ID: {})",
                event.studentId(), event.courseId(), event.enrollmentId());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "notification.enrollment-completed.queue", durable = "true"),
            exchange = @Exchange(value = "enrollment.events", type = "topic"),
            key = "enrollment.completed"
    ))
    public void handleEnrollmentCompleted(EnrollmentCompletedEvent event) {
        log.info("[NOTIFICATION] Congratulations! Student {} completed the course {}. (Enrollment ID: {})",
                event.studentId(), event.courseId(), event.enrollmentId());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "notification.certificate-issued.queue", durable = "true"),
            exchange = @Exchange(value = "certificate.events", type = "topic"),
            key = "certificate.issued"
    ))
    public void handleCertificateIssued(CertificateIssuedEvent event) {
        log.info("[NOTIFICATION] Great news! Your certificate code {} is ready for course {}. (Certificate ID: {})",
                event.certificateCode(), event.courseId(), event.certificateId());
    }
}