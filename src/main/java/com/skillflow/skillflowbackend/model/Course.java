package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.AudioLanguage;
import com.skillflow.skillflowbackend.model.enume.CourseLevel;
import com.skillflow.skillflowbackend.model.enume.CourseStatus;
import com.skillflow.skillflowbackend.model.enume.IntroCourseType;
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
public class Course {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idCourse;
    private String title;
    private String thumbnailUrl;
    @Column(length = 1000)
    private String shortDescription;
    @Lob
    @Column(length = 10000000)
    private String longDescription;
    @Column(length = 1000)
    private String whatWillStudentLearn;
    @Column(length = 1000)
    private String requirements;
    private String targetAudience;
    private int totalDuration;
    private int totalLesson;
    private int totalStudent;
    private int totalReview;
    private double rating;
    private boolean isBestSeller;

    private boolean free;
    private double regularPrice;
    private double discountPrice;

    @Enumerated(EnumType.STRING)
    private IntroCourseType introCourseType;
    private String introCourse;
    @Column(length = 10000000)
    @Lob
    private byte[] thumbnailCourse;
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;
    @Enumerated(EnumType.STRING)
    private AudioLanguage audioLanguage;

    private Instant createdAt;
    private Instant updatedAt;

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
