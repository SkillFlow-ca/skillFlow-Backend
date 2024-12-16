package com.skillflow.skillflowbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillflow.skillflowbackend.model.enume.JobType;
import com.skillflow.skillflowbackend.model.enume.SourceJob;
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
public class Job {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idJob;

    private String title;

    private String description;

    private String companyName;

    private String location;

    private String salary;
    public String keyword;

    @Enumerated(EnumType.STRING)
    private JobType type; // FULL_TIME, PART_TIME, etc.
    @Enumerated(EnumType.STRING)
    private SourceJob sourceJob;

    private Instant postedAt;

    private Instant expiryDate;

    private String scrapedUrl;

    private String jobUrl;

    private Instant createdAt;

    private Instant updatedAt;
    private Boolean isDeleted;

    @ManyToOne
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "job")
    private List<JobScraping> jobScrapingList;
}
