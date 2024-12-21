package com.skillflow.skillflowbackend.specifications;

import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.enume.JobType;
import com.skillflow.skillflowbackend.model.enume.SourceJob;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {
    public static Specification<Job> searchJobByManyConditions(String title,
                                                               SourceJob sourceJob, String companyName, String keyword, JobType type) {
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
                    ? criteriaBuilder.like(root.get("keyword"), "%" + keyword + "%")
                    : criteriaBuilder.conjunction();

            Predicate jobtypePredicate = type != null
                    ? criteriaBuilder.equal(root.get("type"), type)
                    : criteriaBuilder.conjunction();

            Predicate isDeletedPredicate = criteriaBuilder.isFalse(root.get("isDeleted"));

            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(titlePredicate,sourcePredicate, companyPredicate,keywordPredicate,jobtypePredicate, isDeletedPredicate);
        };
    }
}
