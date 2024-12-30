package com.skillflow.skillflowbackend.dto;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Lesson;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ModuleDTO {
    private String name;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne
    private CourseDTO course;
    @OneToMany(mappedBy = "module")
    private List<LessonDTO> lessonList;
}
