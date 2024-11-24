package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.RoleType;
import jakarta.persistence.*;
import lombok.*;

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
    private String profilePicture;
    private LocalDateTime tokenToForgotPasswordCreationDate;
    private LocalDateTime validateCodeCreationDate;
    private Date createdAt;
    private Date updatedAt;
    private RoleType roleType;


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
}
