package dev.rangel.eduflow.certificateservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CertificateTest {

    @Test
    @DisplayName("Should successfully issue a certificate with valid data and correct format")
    void shouldSuccessfullyIssueCertificate() {
        UUID enrollmentId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        Certificate certificate = Certificate.issue(enrollmentId, studentId, courseId);

        assertThat(certificate).isNotNull();
        assertThat(certificate.getEnrollmentId()).isEqualTo(enrollmentId);
        assertThat(certificate.getStudentId()).isEqualTo(studentId);
        assertThat(certificate.getCourseId()).isEqualTo(courseId);
        assertThat(certificate.getIssuedAt()).isNotNull();

        int currentYear = Instant.now().atZone(ZoneId.of("UTC")).getYear();
        String expectedPrefix = "CERT-" + currentYear + "-";

        assertThat(certificate.getCertificateCode()).startsWith(expectedPrefix);
        assertThat(certificate.getCertificateCode()).hasSize(22);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when any required ID is null")
    void shouldThrowExceptionWhenIdIsNull() {
        UUID validId = UUID.randomUUID();

        assertThatThrownBy(() -> Certificate.issue(null, validId, validId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enrollment ID cannot be null");

        assertThatThrownBy(() -> Certificate.issue(validId, null, validId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student ID cannot be null");

        assertThatThrownBy(() -> Certificate.issue(validId, validId, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Course ID cannot be null");
    }
}