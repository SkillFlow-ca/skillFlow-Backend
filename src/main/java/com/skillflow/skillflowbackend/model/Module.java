package com.skillflow.skillflowbackend.model;
import jakarta.persistence.*;
import lombok.*;

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
    private String description;

    @ManyToOne
    private Course course;
    @OneToMany(mappedBy = "module")
    private List<Lesson> lessonList;
}
