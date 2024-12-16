package com.skillflow.skillflowbackend.specifications;

import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.enume.SourceJob;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {
    public static Specification<Job> searchJobByManyConditions(String title,
            SourceJob sourceJob, String companyName,String keyword) {
        return (root, query, criteriaBuilder) -> {
            Predicate titlePredicate = title != null
                    ? criteriaBuilder.like(root.get("title"), "%" + title + "%")
                    : criteriaBuilder.conjunction();

            Predicate sourcePredicate = sourceJob != null
                    ? criteriaBuilder.equal(root.get("sourceJob"), sourceJob)
                    : criteriaBuilder.conjunction();

            Predicate companyPredicate = companyName != null
                    ? criteriaBuilder.like(root.get("companyName"), "%" + companyName + "%")
                    : criteriaBuilder.conjunction();

            Predicate keywordPredicate = keyword != null
                    ? criteriaBuilder.equal(root.get("keyword"), keyword)
                    : criteriaBuilder.conjunction();

            Predicate isDeletedPredicate = criteriaBuilder.isFalse(root.get("isDeleted"));

            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(titlePredicate,sourcePredicate, companyPredicate,keywordPredicate, isDeletedPredicate);
        };
    }
}
