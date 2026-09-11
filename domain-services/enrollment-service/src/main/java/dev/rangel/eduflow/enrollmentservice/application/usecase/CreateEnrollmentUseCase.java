package dev.rangel.eduflow.enrollmentservice.application.usecase;

import dev.rangel.eduflow.enrollmentservice.application.dto.request.CreateEnrollmentRequest;
import dev.rangel.eduflow.enrollmentservice.application.dto.response.EnrollmentResponse;
import dev.rangel.eduflow.enrollmentservice.domain.exception.DuplicateEnrollmentException;
import dev.rangel.eduflow.enrollmentservice.domain.model.Enrollment;
import dev.rangel.eduflow.enrollmentservice.infrastructure.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public EnrollmentResponse execute(CreateEnrollmentRequest dto) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(dto.studentId(), dto.courseId())) {
            throw new DuplicateEnrollmentException(dto.studentId(), dto.courseId());
        }

        Enrollment enrollment = Enrollment.create(dto.studentId(), dto.courseId());
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return EnrollmentResponse.from(savedEnrollment);
    }
}