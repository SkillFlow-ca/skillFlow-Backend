package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Blog;
import org.springframework.data.repository.CrudRepository;

public interface BlogRepository extends CrudRepository<Blog,Long> {
}
