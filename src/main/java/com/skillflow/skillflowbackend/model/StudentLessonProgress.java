package com.skillflow.skillflowbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private double progressPercentage;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    @OneToOne
    private Lesson lesson;
}
