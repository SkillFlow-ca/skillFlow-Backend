package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.service.JobScrapingIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/jobScraping/")
@Validated
public class JobScrapingController {
    @Autowired
    private JobScrapingIService jobScrapingIService;

    @GetMapping("get")
    public List<Job> scrapeJobs(){
        return jobScrapingIService.scrapeJobs();
    }
    @GetMapping("getUSA")
    public List<Job> scrapeJobUSA(){
        return jobScrapingIService.scrapeJobUSA();
    }
}
