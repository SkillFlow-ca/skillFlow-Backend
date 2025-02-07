package com.skillflow.skillflowbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class CourseCategory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idCourseCategory;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isDeleted;
    @JsonIgnore
    @ManyToMany(mappedBy = "courseCategoryList")
    private List<Course> courseList;
}
