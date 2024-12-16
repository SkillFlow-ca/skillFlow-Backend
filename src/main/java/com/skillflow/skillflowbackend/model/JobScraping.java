package com.skillflow.skillflowbackend.model;

import com.skillflow.skillflowbackend.model.enume.ScrapingStatus;
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
public class JobScraping {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idJobScraping;

    private Instant scrapedAt;

    private String scraperName;

    @Enumerated(EnumType.STRING)
    private ScrapingStatus status;
    @Lob
    @Column(length = 10000000)
    private String errorMessage;

    @ManyToOne
    private Job job;

}
