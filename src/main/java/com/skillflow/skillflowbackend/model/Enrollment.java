package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.StatusENR;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class Enrollment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idEnrollment;

    private Date enrollmentDate;
    private StatusENR statusEnr; // Enum: PENDING, ACTIVE, COMPLETED
    private float progress;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    private User user;
    @OneToOne
    private Course course;
}
