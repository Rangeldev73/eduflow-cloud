package dev.rangel.eduflow.courseservice.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_course")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseLevel level;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("moduleOrder ASC")
    @Getter(AccessLevel.NONE)
    private List<Module> modules = new ArrayList<>();

    public Course(String title, String description, CourseLevel level) {
        validateTitle(title);
        this.title = title.trim();
        this.description = description;
        this.level = Objects.requireNonNull(level, "Course level cannot be null");
    }

    public void addModule(String title, String description, int duration) {
        validateTitle(title);
        validateDuration(duration);

        int nextOrder = this.modules.size() + 1;
        Module newModule = new Module(this, title.trim(), description, duration, nextOrder);
        this.modules.add(newModule);
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    public int getTotalWorkload() {
        return this.modules.stream()
                .mapToInt(Module::getDuration)
                .sum();
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
    }

    private void validateDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }
    }
}