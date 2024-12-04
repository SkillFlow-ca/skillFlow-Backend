package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.dto.UserDTO;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.BlogCategory;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.repository.BlogCategoryRepository;
import com.skillflow.skillflowbackend.service.BlogCategoryIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseModel<BlogCategory> getAllBlogCategories(Pageable pageable) {
        Page<BlogCategory> blogCategoryList= blogCategoryRepository.findBlogCategorys(pageable);
        return buildResponse(blogCategoryList);

    }


    private ResponseModel<BlogCategory> buildResponse(Page<BlogCategory> blogCategory) {
        List<BlogCategory> listBlogCategory = blogCategory.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<BlogCategory>builder()
                .pageNo(blogCategory.getNumber())
                .pageSize(blogCategory.getSize())
                .totalElements(blogCategory.getTotalElements())
                .totalPages(blogCategory.getTotalPages())
                .data(listBlogCategory)
                .isLastPage(blogCategory.isLast())
                .build();
    }
}
