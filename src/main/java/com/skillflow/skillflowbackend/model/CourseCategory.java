package com.skillflow.skillflowbackend.model;
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
public class CourseCategory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idCourseCategory;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "courseCategoryList")
    private List<Course> courseList;
}
