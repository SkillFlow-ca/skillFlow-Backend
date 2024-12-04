package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.BlogCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogCategoryIService {
    public BlogCategory saveBlogCategory(BlogCategory blogCategory);
    public BlogCategory updateBlogCategory(long idBlog,BlogCategory blogCategory);
    public void deleteBlogCategory(Long id);
    public ResponseModel<BlogCategory> getAllBlogCategories(Pageable pageable);
}
