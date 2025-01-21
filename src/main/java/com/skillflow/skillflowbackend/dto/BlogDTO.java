package com.skillflow.skillflowbackend.dto;

import com.skillflow.skillflowbackend.model.BlogCategory;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BlogDTO {
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private boolean inLandingPage;

    @Enumerated(EnumType.STRING)
    private StatusBlog statusBlog; // Enum: APPROVED, PENDING, REFUSED
    private boolean isAiGenerated;
    @ManyToOne
    private User admin;

    @ManyToMany
    private List<BlogCategory> blogCategoryList;
}
