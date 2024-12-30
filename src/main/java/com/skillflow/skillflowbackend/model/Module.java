package com.skillflow.skillflowbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Module {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idModule;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne
    private Course course;
    @OneToMany(mappedBy = "module")
    private List<Lesson> lessonList;
}
