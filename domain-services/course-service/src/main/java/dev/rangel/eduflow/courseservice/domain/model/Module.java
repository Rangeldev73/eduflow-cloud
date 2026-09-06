package dev.rangel.eduflow.courseservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "tb_module")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int duration;

    @Column(name = "module_order", nullable = false)
    private int moduleOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    Module(Course course, String title, String description, int duration, int moduleOrder) {
        this.course = course;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.moduleOrder = moduleOrder;
    }
}