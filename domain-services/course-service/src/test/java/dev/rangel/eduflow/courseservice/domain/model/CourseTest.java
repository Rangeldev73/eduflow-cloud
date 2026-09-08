package dev.rangel.eduflow.courseservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseTest {

    @Test
    @DisplayName("Should create course successfully with valid parameters")
    void shouldCreateCourseWithValidParameters() {
        Course course = new Course("Clean Architecture", "Mastering software architecture", CourseLevel.ADVANCED);

        assertEquals("Clean Architecture", course.getTitle());
        assertEquals("Mastering software architecture", course.getDescription());
        assertEquals(CourseLevel.ADVANCED, course.getLevel());
        assertEquals(0, course.getModules().size());
        assertEquals(0, course.getTotalWorkload());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Should throw IllegalArgumentException when creating course with blank title")
    void shouldThrowExceptionWhenCreatingCourseWithBlankTitle(String invalidTitle) {
        assertThrows(IllegalArgumentException.class,
                () -> new Course(invalidTitle, "Valid description", CourseLevel.BEGINNER));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when creating course with null title")
    void shouldThrowExceptionWhenCreatingCourseWithNullTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> new Course(null, "Valid description", CourseLevel.BEGINNER));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when creating course with null level")
    void shouldThrowExceptionWhenCreatingCourseWithNullLevel() {
        assertThrows(IllegalArgumentException.class,
                () -> new Course("Valid Title", "Valid description", null));
    }

    @Test
    @DisplayName("Should assign contiguous sequential order (1, 2, 3) to added modules")
    void shouldAssignContiguousSequentialOrderToAddedModules() {
        Course course = new Course("Java Fundamentals", "Basics of Java", CourseLevel.BEGINNER);

        course.addModule("Syntax", "Basic syntax", 60);
        course.addModule("OOP", "Object oriented programming", 120);
        course.addModule("Collections", "Java Collections Framework", 90);

        assertEquals(3, course.getModules().size());
        assertEquals(1, course.getModules().get(0).getModuleOrder());
        assertEquals(2, course.getModules().get(1).getModuleOrder());
        assertEquals(3, course.getModules().get(2).getModuleOrder());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    @DisplayName("Should throw IllegalArgumentException when adding module with duration <= 0")
    void shouldThrowExceptionWhenAddingModuleWithInvalidDuration(int invalidDuration) {
        Course course = new Course("Docker Basics", "Intro to containers", CourseLevel.INTERMEDIATE);

        assertThrows(IllegalArgumentException.class,
                () -> course.addModule("Architecture", "Overview", invalidDuration));
    }

    @Test
    @DisplayName("Should sum total workload correctly based on all added modules")
    void shouldCalculateTotalWorkloadCorrectly() {
        Course course = new Course("Docker Basics", "Intro to containers", CourseLevel.INTERMEDIATE);

        course.addModule("Module 1", "Images and Containers", 45);
        course.addModule("Module 2", "Volumes and Networks", 75);

        assertEquals(120, course.getTotalWorkload());
    }

    @Test
    @DisplayName("Should not allow external modification of modules list")
    void shouldNotAllowExternalModificationOfModulesList() {
        Course course = new Course("Spring Boot", "Building REST APIs", CourseLevel.INTERMEDIATE);
        course.addModule("Setup", "Initial setup", 30);

        Module fakeModule = new Module(course, "Fake Title", "Fake Desc", 10, 2);

        assertThrows(UnsupportedOperationException.class,
                () -> course.getModules().add(fakeModule));
    }
}