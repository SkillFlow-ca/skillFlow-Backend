package com.skillflow.skillflowbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillflow.skillflowbackend.model.enume.QuestionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class Question {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idQuestion;
    private String title;
    @Lob
    @Column(length = 10000000)
    private String problemDetails;
    @Lob
    @Column(length = 10000000)
    private String whatUTried;
    private String tags;
    private int views;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
    private QuestionStatus questionStatus;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "question")
    private List<Answer> answerList;
}
