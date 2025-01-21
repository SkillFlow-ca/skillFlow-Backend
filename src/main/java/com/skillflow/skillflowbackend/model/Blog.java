package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
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
    @Column(length = 1000)
    private String title;
    @Lob
    @Column(length = 10000000)
    private String content;
    @Column(length = 1000)
    private String shortParagraph;
    private String tags;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean inLandingPage;

    private Boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private StatusBlog statusBlog; // Enum: APPROVED, PENDING, REFUSED
    private boolean isAiGenerated;
    @Column(length = 90000000)
    @Lob
    private byte[] blogPicture;
    @ManyToOne
    private User admin;

    @ManyToMany
    private List<BlogCategory> blogCategoryList;
}
