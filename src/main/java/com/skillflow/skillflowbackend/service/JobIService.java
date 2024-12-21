package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.enume.JobType;
import com.skillflow.skillflowbackend.model.enume.SourceJob;
import org.springframework.data.domain.Pageable;

public interface JobIService {
    public Job saveJob(Job job);
    public Job getJobById(long id);
    public Job updateJob(Job job,long idJob);
    public void deleteJob(long idJob);
    public ResponseModel<Job> getJobsAdminByContraints(String title, SourceJob sourceJob, String companyName, String keyword, JobType type, Pageable pageable);
}
