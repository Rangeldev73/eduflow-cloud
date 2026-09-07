package dev.rangel.eduflow.courseservice.domain.model;

import dev.rangel.eduflow.courseservice.infrastructure.persistence.repository.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CourseConcurrencyTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @DisplayName("Tests whether OPTIMISTIC_FORCE_INCREMENT forces version increment and triggers OptimisticLockingFailureException")
    void shouldVerifyVersionIncrementAndOptimisticLockingOnAddModule() throws Exception {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        Course course = tx.execute(status -> {
            Course c = new Course("Advanced Java", "Java Course", CourseLevel.ADVANCED);
            return courseRepository.save(c);
        });

        Long initialVersion = course.getVersion();
        assertThat(initialVersion).isNotNull();

        CountDownLatch thread1Started = new CountDownLatch(1);
        CountDownLatch thread1Finished = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(2);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        AtomicReference<Exception> thread1Exception = new AtomicReference<>();
        AtomicReference<Exception> thread2Exception = new AtomicReference<>();

        executor.submit(() -> {
            try {
                tx.executeWithoutResult(status -> {
                    Course c1 = courseRepository.findByIdWithOptimisticLock(course.getId()).orElseThrow();
                    thread1Started.countDown();

                    c1.addModule("Module 1", "Description 1", 10);
                    courseRepository.saveAndFlush(c1);
                });
            } catch (Exception e) {
                thread1Exception.set(e);
            } finally {
                thread1Finished.countDown();
                finishLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                thread1Started.await();

                tx.executeWithoutResult(status -> {
                    Course c2 = courseRepository.findByIdWithOptimisticLock(course.getId()).orElseThrow();

                    try {
                        thread1Finished.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    c2.addModule("Module 2", "Description 2", 15);
                    courseRepository.saveAndFlush(c2);
                });
            } catch (Exception e) {
                thread2Exception.set(e);
            } finally {
                finishLatch.countDown();
            }
        });

        finishLatch.await();
        executor.shutdown();

        System.out.println("--- CONCURRENCY TEST RESULT ---");
        System.out.println("Thread 1 Exception: " + thread1Exception.get());
        System.out.println("Thread 2 Exception: " + thread2Exception.get());

        boolean threwOptimisticLock = (thread1Exception.get() instanceof ObjectOptimisticLockingFailureException)
                || (thread2Exception.get() instanceof ObjectOptimisticLockingFailureException)
                || (thread2Exception.get() != null && thread2Exception.get().getCause() instanceof ObjectOptimisticLockingFailureException);

        assertThat(threwOptimisticLock)
                .as("One of the threads should have thrown ObjectOptimisticLockingFailureException")
                .isTrue();
    }
}