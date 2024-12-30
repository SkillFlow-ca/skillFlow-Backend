package com.skillflow.skillflowbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillflow.skillflowbackend.model.enume.TypeLesson;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Lesson {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idLesson;

    private String title;
    private String content;
    private String duration;
    private String urlvideoLesson;
    private String urlPdfLesson;
    private String titleVideo;
    private String titlePdf;
    @Transient
    private MultipartFile videoFile;

    private TypeLesson typeLesson;
    private Boolean isPreview;
    private Boolean isPublished;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;

    @JsonIgnore
    @ManyToOne
    private Module module;
    @OneToMany(mappedBy = "lesson")
    private List<LessonRessource> lessonRessourceList;
    @OneToOne(mappedBy = "lesson")
    private StudentLessonProgress studentLessonProgress;
}
