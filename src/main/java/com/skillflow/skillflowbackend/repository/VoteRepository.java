package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote,Long> {
}
