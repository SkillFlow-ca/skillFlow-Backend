package com.skillflow.skillflowbackend.model;

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
public class Contact {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idContact;

    private String name;
    private String email;
    private String subject;
    @Column(length = 1000)
    private String message;
    private boolean isRead;
    private boolean isAnswered;
    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isDeleted;

}
