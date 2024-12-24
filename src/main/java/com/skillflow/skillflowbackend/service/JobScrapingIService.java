package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.Job;

import java.util.List;

public interface JobScrapingIService {
    public List<Job> scrapeJobs();
    public List<Job> scrapeJobUSA();

}
