package dev.rangel.eduflow.courseservice.infrastructure.persistence.repository;

import dev.rangel.eduflow.courseservice.domain.model.Course;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT c FROM Course c WHERE c.id = :id")
    Optional<Course> findByIdWithOptimisticLock(@Param("id") UUID id);
}