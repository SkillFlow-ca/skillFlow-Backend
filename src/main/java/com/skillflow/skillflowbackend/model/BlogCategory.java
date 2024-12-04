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
public class BlogCategory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idBlogCategory;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    @JsonIgnore
    @ManyToMany(mappedBy = "blogCategoryList")
    private List<Blog> blogList;
}
