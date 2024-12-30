package com.skillflow.skillflowbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillflow.skillflowbackend.model.LessonRessource;
import com.skillflow.skillflowbackend.model.Module;
import com.skillflow.skillflowbackend.model.StudentLessonProgress;
import com.skillflow.skillflowbackend.model.enume.TypeLesson;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
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
public class LessonDTO {
    private String title;
    private String content;
    private String duration;
    private String urlvideoLesson;
    private String urlPdfLesson;
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
