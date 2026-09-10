package dev.rangel.eduflow.enrollmentservice.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "tb_enrollment",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_student_course", columnNames = {"student_id", "course_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "student_id", nullable = false)
        private UUID studentId;

        @Column(name = "course_id", nullable = false)
        private UUID courseId;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private EnrollmentStatus status;

        @Column(name = "created_at", nullable = false, updatable = false)
        private Instant createdAt;

        @Version
        private Long version;

        private Enrollment(UUID studentId, UUID courseId) {
                validateStudentId(studentId);
                validateCourseId(courseId);
                this.studentId = studentId;
                this.courseId = courseId;
                this.status = EnrollmentStatus.ACTIVE;
                this.createdAt = Instant.now();
        }

        public static Enrollment create(UUID studentId, UUID courseId) {
                return new Enrollment(studentId, courseId);
        }

        public void complete() {
                if (this.status != EnrollmentStatus.ACTIVE) {
                        throw new IllegalStateException("Only active enrollments can be completed");
                }
                this.status = EnrollmentStatus.COMPLETED;
        }

        public void cancel() {
                if (this.status != EnrollmentStatus.ACTIVE) {
                        throw new IllegalStateException("Only active enrollments can be cancelled");
                }
                this.status = EnrollmentStatus.CANCELLED;
        }

        private void validateStudentId(UUID studentId) {
                if (studentId == null) {
                        throw new IllegalArgumentException("Student ID cannot be null");
                }
        }

        private void validateCourseId(UUID courseId) {
                if (courseId == null) {
                        throw new IllegalArgumentException("Course ID cannot be null");
                }
        }
}