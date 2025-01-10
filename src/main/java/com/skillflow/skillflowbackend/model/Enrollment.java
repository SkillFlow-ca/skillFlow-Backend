package com.skillflow.skillflowbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillflow.skillflowbackend.model.enume.StatusENR;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
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

    private Instant enrollmentDate;
    private StatusENR statusEnr; // Enum: PENDING, ACTIVE, COMPLETED
    private float progress;
    private Instant createdAt;
    private Instant updatedAt;
    @JsonIgnore
    @ManyToOne
    private User user;
    @ManyToOne
    private Course course;
}
