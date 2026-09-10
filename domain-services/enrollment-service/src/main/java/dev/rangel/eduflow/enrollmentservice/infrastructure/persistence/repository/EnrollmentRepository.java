package dev.rangel.eduflow.enrollmentservice.infrastructure.persistence.repository;

import dev.rangel.eduflow.enrollmentservice.domain.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);

}