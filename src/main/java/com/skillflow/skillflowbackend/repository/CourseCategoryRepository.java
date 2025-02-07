package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.CourseCategory;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCategoryRepository extends CrudRepository<CourseCategory, Long>{
    Page<CourseCategory> findByIsDeletedFalse(Pageable pageable);
    @Query("SELECT c FROM CourseCategory c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    public List<CourseCategory> findByCourseCategoryAndIsDeletedFalseOrderByCreatedAtDesc();
}
