package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.BlogCategory;
import com.skillflow.skillflowbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BlogCategoryRepository extends CrudRepository<BlogCategory,Long> {
    @Query("select bc from BlogCategory bc ")
    Page<BlogCategory> findBlogCategorys(Pageable pageable);
}
