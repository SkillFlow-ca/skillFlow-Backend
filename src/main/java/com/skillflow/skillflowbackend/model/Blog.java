package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class Blog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idBlog;

    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private StatusBlog statusBlog; // Enum: APPROVED, PENDING, REFUSED
    private boolean isAiGenerated;
    @ManyToOne
    private User admin;

    @ManyToMany
    private List<BlogCategory> blogCategoryList;
}
