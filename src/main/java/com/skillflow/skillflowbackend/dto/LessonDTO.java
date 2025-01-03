package com.skillflow.skillflowbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillflow.skillflowbackend.model.LessonRessource;
import com.skillflow.skillflowbackend.model.Module;
import com.skillflow.skillflowbackend.model.StudentLessonProgress;
import com.skillflow.skillflowbackend.model.enume.TypeLesson;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LessonDTO {
    private long idLesson;
    private String reference;
    private String title;
    private String content;
    private String duration;
    private String urlvideoLesson;
    private String urlPdfLesson;
    private String titleVideo;
    private String titlePdf;

    private TypeLesson typeLesson;
    private Boolean isPreview;
    private Boolean isPublished;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;
    @JsonIgnore
    @ManyToOne
    private Module module;
    @OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER)
    private List<LessonRessource> lessonRessourceList;
    @OneToOne(mappedBy = "lesson", fetch = FetchType.EAGER)
    private StudentLessonProgress studentLessonProgress;
}
