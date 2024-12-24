package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Candidate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate,Long> {
    public Candidate findByTrackCode(String trackCode);
}
