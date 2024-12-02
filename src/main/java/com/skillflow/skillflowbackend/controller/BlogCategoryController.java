package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.model.BlogCategory;
import com.skillflow.skillflowbackend.service.BlogCategoryIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/blogCategory/")
public class BlogCategoryController {
    @Autowired
    private BlogCategoryIService blogCategoryIService;

    @PostMapping("/create")
    public BlogCategory createBlogCategory(@Validated @RequestBody BlogCategory blogCategory) {
        return blogCategoryIService.saveBlogCategory(blogCategory);
    }
    @PutMapping("/update")
    public BlogCategory updateBlogCategory(@RequestParam Long id, @Validated @RequestBody BlogCategory blogCategory) {
        return blogCategoryIService.updateBlogCategory(id, blogCategory);
    }
    @GetMapping("getAll")
    public List<BlogCategory> getAllBlogCategories() {
        return blogCategoryIService.getAllBlogCategories();
    }
    @DeleteMapping("/delete")
    public void deleteBlogCategory(@RequestParam Long id) {
        blogCategoryIService.deleteBlogCategory(id);
    }
}
