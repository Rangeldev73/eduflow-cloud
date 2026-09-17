package dev.rangel.eduflow.certificateservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Table(
        name = "tb_certificate",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_certificate_enrollment", columnNames = {"enrollment_id"}),
                @UniqueConstraint(name = "uk_certificate_code", columnNames = {"certificate_code"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "enrollment_id", nullable = false, unique = true)
    private UUID enrollmentId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "course_id", nullable = false)
    private UUID courseId;

    @Column(name = "certificate_code", nullable = false, unique = true)
    private String certificateCode;

    @Column(name = "issued_at", nullable = false, updatable = false)
    private Instant issuedAt;

    private Certificate(UUID enrollmentId, UUID studentId, UUID courseId) {
        validateNotNull(enrollmentId, "Enrollment ID");
        validateNotNull(studentId, "Student ID");
        validateNotNull(courseId, "Course ID");

        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.issuedAt = Instant.now();
        this.certificateCode = generateCertificateCode(issuedAt);
    }

    public static Certificate issue(UUID enrollmentId, UUID studentId, UUID courseId) {
        return new Certificate(enrollmentId, studentId, courseId);
    }

    private String generateCertificateCode(Instant now) {
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        int year = now.atZone(ZoneId.of("UTC")).getYear();
        return "CERT-" + year + "-" + randomPart;
    }

    private void validateNotNull(UUID value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}