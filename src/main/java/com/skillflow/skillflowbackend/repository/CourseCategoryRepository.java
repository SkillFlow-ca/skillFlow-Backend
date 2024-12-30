package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.CourseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCategoryRepository extends CrudRepository<CourseCategory, Long>{
    Page<CourseCategory> findByIsDeletedFalse(Pageable pageable);
}
