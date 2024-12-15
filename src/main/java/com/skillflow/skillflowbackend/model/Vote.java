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
public class Vote {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idVote;
    private boolean isUpVote;
    private boolean isDownVote;
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToOne
    private User user;
    @JsonIgnore
    @ManyToOne
    private Answer answer;

}
