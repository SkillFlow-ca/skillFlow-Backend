package com.skillflow.skillflowbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillflow.skillflowbackend.model.enume.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
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
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Boolean isValidated;
    private Boolean isDeleted;
    private Long tokenToValidate;
    private Long tokenToForgotPassword;
    @Column(length = 10000000)
    @Lob
    private byte[] profilePicture;
    private LocalDateTime tokenToForgotPasswordCreationDate;
    private LocalDateTime validateCodeCreationDate;
    private Instant createdAt;
    private Instant updatedAt;
    @Enumerated(EnumType.STRING)
    private RoleType roleTypes;


    @OneToMany(mappedBy = "admin")
    private List<Blog> blogList;

    @OneToMany(mappedBy = "user")
    private List<Payment> paymentList;

    @OneToMany(mappedBy = "admin")
    private List<Course> courseList;

    @OneToMany(mappedBy = "user")
    private List<Enrollment> enrollmentList;
    @OneToMany(mappedBy = "user")
    private List<StudentLessonProgress> studentLessonProgressList;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Question> questionList;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Answer> answerList;
}
