package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.enume.JobType;
import com.skillflow.skillflowbackend.model.enume.SourceJob;
import com.skillflow.skillflowbackend.repository.JobRepository;
import com.skillflow.skillflowbackend.service.JobIService;
import com.skillflow.skillflowbackend.specifications.JobSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService implements JobIService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SessionService sessionService;

    @Override
    public Job saveJob(Job job) {
        job.setCreatedAt(Instant.now());
        job.setSourceJob(SourceJob.MANUAL);
        job.setUser(sessionService.getUserBySession().get());
        job.setIsDeleted(false);
        return jobRepository.save(job);
    }

    @Override
    public Job getJobById(long id) {
        Job job= jobRepository.findById(id).get();
        return job;
    }

    @Override
    public Job updateJob(Job job, long idJob) {
        Job job1= jobRepository.findById(idJob).get();
        job1.setTitle(job.getTitle());
        job1.setCompanyName(job.getCompanyName());
        job1.setLocation(job.getLocation());
        job1.setSalary(job.getSalary());
        job1.setDescription(job.getDescription());
        job1.setSourceJob(job.getSourceJob());
        job1.setJobUrl(job.getJobUrl());
        job1.setCreatedAt(job.getCreatedAt());
        job1.setScrapedUrl(job.getScrapedUrl());
        job1.setUser(job.getUser());
        job1.setUpdatedAt(Instant.now());
        return jobRepository.save(job1);
    }

    @Override
    public void deleteJob(long idJob) {
        Job job= jobRepository.findById(idJob).get();
        job.setIsDeleted(true);
        jobRepository.save(job);
    }

    @Override
    public ResponseModel<Job> getJobsAdminByContraints(String title, SourceJob sourceJob, String companyName, String keyword, JobType type, Pageable pageable) {
        final Specification<Job> specification= JobSpecification.searchJobByManyConditions(title,sourceJob,companyName,keyword,type);
        Page<Job> jobs = jobRepository.findAll(specification, pageable);
        return buildResponse(jobs);
    }

    private ResponseModel<Job> buildResponse(Page<Job> job) {
        List<Job> listJob = job.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<Job>builder()
                .pageNo(job.getNumber())
                .pageSize(job.getSize())
                .totalElements(job.getTotalElements())
                .totalPages(job.getTotalPages())
                .data(listJob)
                .isLastPage(job.isLast())
                .build();
    }
}
