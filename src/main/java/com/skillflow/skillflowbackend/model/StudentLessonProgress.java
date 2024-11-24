package com.skillflow.skillflowbackend.model;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class StudentLessonProgress {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idStudentLessonProgress;
    private boolean completed;
    private String notes;
    private float progress;
    @ManyToOne
    private User user;
    @OneToOne
    private Lesson lesson;
}
