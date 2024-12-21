package com.skillflow.skillflowbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillflow.skillflowbackend.model.enume.CandidateStatus;
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
public class Candidate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idCandidate;

    private String firstName;
    private String lastName;
    @Column(length = 10000000)
    @Lob
    private byte[] resume;
    private String email;
    private String phoneNumber;
    private String trackCode;
    @Enumerated(EnumType.STRING)
    private CandidateStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
    @JsonIgnore
    @ManyToOne
    private Job job;
}
