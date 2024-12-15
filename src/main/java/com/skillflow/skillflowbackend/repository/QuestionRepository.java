package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends CrudRepository<Question,Long> {
    @Query("SELECT q FROM Question q WHERE q.user.idUser = :v1 and q.isDeleted=false  order by q.createdAt desc")
    public Page<Question> getMyQuestions(@Param("v1") long idUser, Pageable pageable);
    Page<Question> findAll(Specification<Question> specification, Pageable pageable);
    @Query("SELECT COUNT(q) FROM Question q WHERE q.isDeleted = false")
    long countNotDeleted();
}
