package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.Question;
import com.skillflow.skillflowbackend.model.enume.SourceJob;
import com.skillflow.skillflowbackend.service.JobIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/job/")
@Validated
public class JobController {
    @Autowired
    private JobIService jobIService;

    @PostMapping("add")
    public Job saveJob(@RequestBody Job job) {
        return jobIService.saveJob(job);
    }
    @GetMapping("get")
    public Job getJobById(@RequestParam long id) {
        return jobIService.getJobById(id);
    }
    @PutMapping("update")
    public Job updateJob(@RequestBody Job job, @RequestParam long id) {
        return jobIService.updateJob(job, id);
    }
    @PutMapping("delete")
    public void deleteJob(@RequestParam long id) {
        jobIService.deleteJob(id);
    }
    @GetMapping("/getJobsAdminByContraints")
    public ResponseModel<Job> getJobsAdminByContraints(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) SourceJob sourceJob,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false,defaultValue="1")int pageNo,
            @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<Job> jobs= jobIService.getJobsAdminByContraints(title,sourceJob,companyName,keyword,pageRequestData);
        return jobs;
    }
}
