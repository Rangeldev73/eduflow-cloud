package dev.rangel.eduflow.certificateservice.infrastructure.persistence.repository;

import dev.rangel.eduflow.certificateservice.domain.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    Optional<Certificate> findByEnrollmentId(UUID enrollmentId);
}