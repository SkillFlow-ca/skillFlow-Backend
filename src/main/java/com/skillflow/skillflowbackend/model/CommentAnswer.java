package com.skillflow.skillflowbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class CommentAnswer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idComment;
    @Lob
    @Column(length = 10000000)
    @NotBlank(message = "Invalid firstName: Empty firstName")
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
    @ManyToOne
    private User user;
    @JsonIgnore
    @ManyToOne
    private Answer answer;
}
