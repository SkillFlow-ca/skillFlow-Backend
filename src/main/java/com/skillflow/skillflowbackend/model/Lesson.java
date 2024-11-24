package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.TypeLesson;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class Lesson {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idLesson;

    private String title;
    private String content;
    private String duration;
    private String urlLesson;
    private TypeLesson typeLesson;
    @ManyToOne
    private Module module;
    @OneToMany(mappedBy = "lesson")
    private List<LessonRessource> lessonRessourceList;
    @OneToOne(mappedBy = "lesson")
    private StudentLessonProgress studentLessonProgress;
}
