package com.skillflow.skillflowbackend.dto;

import com.skillflow.skillflowbackend.model.CourseCategory;
import com.skillflow.skillflowbackend.model.Enrollment;
import com.skillflow.skillflowbackend.model.PaymentSkillFlow;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.AudioLanguage;
import com.skillflow.skillflowbackend.model.enume.CourseLevel;
import com.skillflow.skillflowbackend.model.enume.CourseStatus;
import com.skillflow.skillflowbackend.model.enume.IntroCourseType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CourseDTO {
    @NotBlank(message = "Invalid title: Empty title")
    @NotNull(message = "Invalid title: title is NULL")
    private String title;
    private String reference;

    private String thumbnailUrl;
    @NotBlank(message = "Invalid shortDescription: Empty shortDescription")
    @NotNull(message = "Invalid shortDescription: shortDescription is NULL")
    @Column(length = 1000)
    private String shortDescription;
    @NotBlank(message = "Invalid longDescription: Empty longDescription")
    @NotNull(message = "Invalid longDescription: longDescription is NULL")
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
    private boolean inLandingPage;


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
    private PaymentSkillFlow paymentSkillFlow;
    @OneToOne(mappedBy = "course")
    private Enrollment enrollment;
    @ManyToMany
    private List<CourseCategory> courseCategoryList;

    @OneToMany(mappedBy = "course")
    private List<ModuleDTO> moduleList;
}
