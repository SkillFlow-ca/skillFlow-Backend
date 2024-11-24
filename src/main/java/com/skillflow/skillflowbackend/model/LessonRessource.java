package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.TypeResource;
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
public class LessonRessource {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idLessonRessource;
    private String title;
    private String urlStorage;
    private int orderIndex;
    private TypeResource typeResource ;
    @ManyToOne
    private Lesson lesson;
}
