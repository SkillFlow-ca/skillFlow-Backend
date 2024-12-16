package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {
    boolean existsByTitleAndCompanyName(String title, String companyName);
    Page<Job> findAll(Specification<Job> specification, Pageable pageable);
}
