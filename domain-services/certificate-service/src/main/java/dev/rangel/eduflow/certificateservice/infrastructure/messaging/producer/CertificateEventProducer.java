package dev.rangel.eduflow.certificateservice.infrastructure.messaging.producer;

import dev.rangel.eduflow.certificateservice.infrastructure.messaging.event.CertificateIssuedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import static dev.rangel.eduflow.certificateservice.infrastructure.messaging.config.RabbitMQConfig.EXCHANGE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendCertificateIssued(CertificateIssuedEvent event) {

        try {
            log.info("Publishing CertificateIssued event for certificate code: {} and enrollment ID: {}",
                    event.certificateCode(), event.enrollmentId());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME,"certificate.issued",event);
        } catch (Exception e) {
            log.error("Failed to publish CertificateIssued event for enrollment ID: {}. Reason: {}",
                    event.enrollmentId(), e.getMessage());
        }
    }
}