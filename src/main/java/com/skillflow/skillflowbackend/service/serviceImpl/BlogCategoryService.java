package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.BlogCategory;
import com.skillflow.skillflowbackend.repository.BlogCategoryRepository;
import com.skillflow.skillflowbackend.service.BlogCategoryIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BlogCategoryService implements BlogCategoryIService{
    @Autowired
    private BlogCategoryRepository blogCategoryRepository;
    @Override
    public BlogCategory saveBlogCategory(BlogCategory blogCategory) {
        blogCategory.setCreatedAt(Instant.now());
        return blogCategoryRepository.save(blogCategory);
    }

    @Override
    public BlogCategory updateBlogCategory(long idBlog, BlogCategory blogCategory) {
        return blogCategoryRepository.findById(idBlog).map(existingBlogCategory -> {
            existingBlogCategory.setName(blogCategory.getName());
            existingBlogCategory.setDescription(blogCategory.getDescription());
            existingBlogCategory.setUpdatedAt(Instant.now());
            return blogCategoryRepository.save(existingBlogCategory);
        }).orElse(null);
    }

    @Override
    public void deleteBlogCategory(Long id) {
        blogCategoryRepository.deleteById(id);
    }

    @Override
    public List<BlogCategory> getAllBlogCategories() {
        return (List<BlogCategory>) blogCategoryRepository.findAll();
    }
}
