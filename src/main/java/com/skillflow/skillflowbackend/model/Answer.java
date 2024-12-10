package com.skillflow.skillflowbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class Answer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idAnswer;
    @Lob
    @Column(length = 10000000)
    private String content;
    private int votes;
    private Boolean isAccepted;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
    @JsonIgnore
    @ManyToOne
    private Question question;
    @ManyToOne
    private User user;
}
