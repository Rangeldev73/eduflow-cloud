package dev.rangel.eduflow.enrollmentservice.application.usecase;

import dev.rangel.eduflow.enrollmentservice.application.dto.request.CreateEnrollmentRequest;
import dev.rangel.eduflow.enrollmentservice.application.dto.response.EnrollmentResponse;
import dev.rangel.eduflow.enrollmentservice.application.event.EnrollmentCreatedEvent;
import dev.rangel.eduflow.enrollmentservice.domain.exception.CourseNotFoundException;
import dev.rangel.eduflow.enrollmentservice.domain.exception.DuplicateEnrollmentException;
import dev.rangel.eduflow.enrollmentservice.domain.model.Enrollment;
import dev.rangel.eduflow.enrollmentservice.infrastructure.client.CourseClient;
import dev.rangel.eduflow.enrollmentservice.infrastructure.messaging.producer.EnrollmentEventProducer;
import dev.rangel.eduflow.enrollmentservice.infrastructure.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentEventProducer eventProducer;
    private final CourseClient courseClient;

    @Transactional
    public EnrollmentResponse execute(CreateEnrollmentRequest dto) {
        if (!courseClient.existsById(dto.courseId())) {
            throw new CourseNotFoundException("Course with ID " + dto.courseId() + " does not exist.");
        }

        if (enrollmentRepository.existsByStudentIdAndCourseId(dto.studentId(), dto.courseId())) {
            throw new DuplicateEnrollmentException(dto.studentId(), dto.courseId());
        }

        Enrollment enrollment = Enrollment.create(dto.studentId(), dto.courseId());
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        EnrollmentCreatedEvent event = new EnrollmentCreatedEvent(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getCreatedAt()
        );
        eventProducer.sendEnrollmentCreated(event);

        return EnrollmentResponse.from(savedEnrollment);
    }
}