package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.BlogCategory;

import java.util.List;

public interface BlogCategoryIService {
    public BlogCategory saveBlogCategory(BlogCategory blogCategory);
    public BlogCategory updateBlogCategory(long idBlog,BlogCategory blogCategory);
    public void deleteBlogCategory(Long id);
    public List<BlogCategory> getAllBlogCategories();
}
