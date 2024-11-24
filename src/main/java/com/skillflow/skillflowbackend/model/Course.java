package com.skillflow.skillflowbackend.model;
import jakarta.persistence.*;
import lombok.*;

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
public class Course {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idCourse;
    private String title;
    private String thumbnailUrl;
    private String description;
    private double price;
    private Date createdAt;
    private Date updatedAt;

    private Boolean isDeleted;
    private Boolean isPublished;

    @ManyToOne
    private User admin;

    @OneToOne(mappedBy = "course")
    private Payment payment;
    @OneToOne(mappedBy = "course")
    private Enrollment enrollment;
    @ManyToMany
    private List<CourseCategory> courseCategoryList;

    @OneToMany(mappedBy = "course")
    private List<Module> moduleList;
}
