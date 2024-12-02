package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.BlogCategory;
import org.springframework.data.repository.CrudRepository;

public interface BlogCategoryRepository extends CrudRepository<BlogCategory,Long> {
}
