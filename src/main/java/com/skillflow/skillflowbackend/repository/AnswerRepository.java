package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Answer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer,Long> {
    @Query("select a from Answer a where a.question.idQuestion = :v1 and a.isDeleted = false")
    public List<Answer> getAnswersByQuestionId(@Param("v1") long idQuestion);
}
