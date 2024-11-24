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
public class BlogCategory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idBlogCategory;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "blogCategoryList")
    private List<Blog> blogList;
}
