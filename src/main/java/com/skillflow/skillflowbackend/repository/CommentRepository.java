package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.CommentAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<CommentAnswer,Long> {
}
