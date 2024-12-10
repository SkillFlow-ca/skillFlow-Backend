package com.skillflow.skillflowbackend.specifications;

import com.skillflow.skillflowbackend.model.Question;
import com.skillflow.skillflowbackend.model.enume.QuestionStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;



public class QuestionSpecification {
    public static Specification<Question> searchQuestionByManyConditions(
            QuestionStatus questionStatus, String questionTitle, Integer views) {
        return (root, query, criteriaBuilder) -> {

            Predicate statusPredicate = questionStatus != null
                    ? criteriaBuilder.equal(root.get("questionStatus"), questionStatus)
                    : criteriaBuilder.conjunction();

            Predicate titlePredicate = questionTitle != null
                    ? criteriaBuilder.like(root.get("title"), "%" + questionTitle + "%")
                    : criteriaBuilder.conjunction();

            Predicate viewsPredicate = views != null
                    ? criteriaBuilder.equal(root.get("views"), views)
                    : criteriaBuilder.conjunction();
            Predicate isDeletedPredicate = criteriaBuilder.isFalse(root.get("isDeleted"));

            return criteriaBuilder.and(statusPredicate, titlePredicate, viewsPredicate,isDeletedPredicate);
        };
    }
}
