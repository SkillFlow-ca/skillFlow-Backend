package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.JobScraping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobScrapingRepository extends CrudRepository<JobScraping, Long> {
}
