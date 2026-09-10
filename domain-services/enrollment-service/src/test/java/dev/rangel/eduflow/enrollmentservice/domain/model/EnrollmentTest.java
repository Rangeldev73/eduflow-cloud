package dev.rangel.eduflow.enrollmentservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnrollmentTest {

    private final UUID studentId = UUID.randomUUID();
    private final UUID courseId = UUID.randomUUID();

    @Test
    @DisplayName("Should create enrollment successfully with active status and timestamp")
    void shouldCreateEnrollmentSuccessfully() {
        Enrollment enrollment = Enrollment.create(studentId, courseId);

        assertEquals(studentId, enrollment.getStudentId());
        assertEquals(courseId, enrollment.getCourseId());
        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
        assertNotNull(enrollment.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when creating enrollment with null studentId")
    void shouldThrowExceptionWhenStudentIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Enrollment.create(null, courseId));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when creating enrollment with null courseId")
    void shouldThrowExceptionWhenCourseIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Enrollment.create(studentId, null));
    }

    @Test
    @DisplayName("Should complete active enrollment successfully")
    void shouldCompleteActiveEnrollment() {
        Enrollment enrollment = Enrollment.create(studentId, courseId);
        enrollment.complete();

        assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
    }

    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, names = {"COMPLETED", "CANCELLED"})
    @DisplayName("Should throw IllegalStateException when completing non-active enrollment")
    void shouldThrowExceptionWhenCompletingNonActiveEnrollment(EnrollmentStatus initialStatus) {
        Enrollment enrollment = Enrollment.create(studentId, courseId);

        if (initialStatus == EnrollmentStatus.COMPLETED) {
            enrollment.complete();
        } else {
            enrollment.cancel();
        }

        assertThrows(IllegalStateException.class, enrollment::complete);
    }

    @Test
    @DisplayName("Should cancel active enrollment successfully")
    void shouldCancelActiveEnrollment() {
        Enrollment enrollment = Enrollment.create(studentId, courseId);
        enrollment.cancel();

        assertEquals(EnrollmentStatus.CANCELLED, enrollment.getStatus());
    }

    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, names = {"COMPLETED", "CANCELLED"})
    @DisplayName("Should throw IllegalStateException when cancelling non-active enrollment")
    void shouldThrowExceptionWhenCancellingNonActiveEnrollment(EnrollmentStatus initialStatus) {
        Enrollment enrollment = Enrollment.create(studentId, courseId);

        if (initialStatus == EnrollmentStatus.COMPLETED) {
            enrollment.complete();
        } else {
            enrollment.cancel();
        }

        assertThrows(IllegalStateException.class, enrollment::cancel);
    }
}