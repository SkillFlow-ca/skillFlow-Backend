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
@Builder
@EqualsAndHashCode
@Entity
public class Lesson {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idLesson;

    private String reference;
    private String title;
    @Lob
    @Column(length = 10000000)
    private String content;
    private String duration;
    private String urlvideoLesson;
    private String urlPdfLesson;
    private String titleVideo;
    private String titlePdf;
    @Enumerated(EnumType.STRING)
    private TypeLesson typeLesson;
    private Boolean isPreview;
    private Boolean isPublished;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;

    @JsonIgnore
    @ManyToOne
    private Module module;
    @JsonIgnore
    @OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER)
    private List<LessonRessource> lessonRessourceList;
    @JsonIgnore
    @OneToOne(mappedBy = "lesson", fetch = FetchType.EAGER)
    private StudentLessonProgress studentLessonProgress;
}
