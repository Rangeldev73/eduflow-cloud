package dev.rangel.eduflow.enrollmentservice.infrastructure.client;

import dev.rangel.eduflow.enrollmentservice.domain.exception.CourseServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import java.util.UUID;

@Slf4j
@Component
public class CourseClient {

    private final RestClient restClient;

    public CourseClient(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder loadBalancedRestClientBuilder) {
        this.restClient = loadBalancedRestClientBuilder
                .baseUrl("http://course-service")
                .build();
    }

    @CircuitBreaker(name = "courseService", fallbackMethod = "existsByIdFallback")
    @Retry(name = "courseService")
    public boolean existsById(UUID courseId) {
        try {
            restClient.get()
                    .uri("/courses/{id}", courseId)
                    .retrieve()
                    .toBodilessEntity();

            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            log.error("Failed to communicate with course-service for courseId {}: {}", courseId, e.getMessage());
            throw new CourseServiceUnavailableException("Course service is temporarily unavailable.");
        }
    }

    public boolean existsByIdFallback(UUID courseId, Throwable t) {
        log.error("Circuit breaker fallback triggered for courseId {}. Reason: {}", courseId, t.getMessage());
        throw new CourseServiceUnavailableException("Course service is currently unavailable. Please try again later.");
    }
}