package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface BlogRepository extends CrudRepository<Blog,Long> {
    Page<Blog> findByStatusBlogAndIsDeletedFalseOrderByCreatedAtDesc(StatusBlog status, Pageable pageable);
}
