package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.BlogCategory;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import com.skillflow.skillflowbackend.service.BlogIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/blog/")
public class BlogController {
    @Autowired
    private BlogIService blogIService;
    @PostMapping("create")
    public List<Blog> create3BlogWitAI() {
        return blogIService.create3BlogWitAI();
    }
    @GetMapping("get")
    public Blog getBlogById(@RequestParam long idBlog) {
        return blogIService.getBlogById(idBlog);
    }
    @GetMapping("getAllBlogBySatusAndOrderedByCreatedAt")
    public ResponseModel<Blog> getAllBlog(@RequestParam StatusBlog statusBlog,
                                          @RequestParam(required = false,defaultValue="1")int pageNo,
                                          @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<Blog> blogs= blogIService.getBlogsByStatusOrderByCreatedAt(statusBlog,pageRequestData);
        return blogs;
    }
    @PutMapping("update")
    public Blog updateBlog(@RequestParam long id, @RequestBody Blog blog) {
        return blogIService.updateBlog(id, blog);
    }
    @PutMapping("updateImage")
    public Blog updateBlogToAddImage(@RequestParam long idBlog, MultipartFile img) throws IOException {
        return blogIService.updateBlogToAddImage(idBlog, img);
    }
    @PutMapping("delete")
    public void deleteBlog(@RequestParam long id){
        blogIService.deleteBlog(id);
    }
    @PutMapping("changeStatus")
    public Blog changeStatusOfBlog(@RequestParam long idBlog,@RequestParam StatusBlog statusBlog) {
        return blogIService.changeStatusOfBlog(idBlog, statusBlog);
    }
    @PutMapping("updateInLandingPage")
    public void updateBlogInLandingPage(@RequestParam long idBlog,@RequestParam boolean inLandingPage) {
        blogIService.updateBlogInLandingPage(idBlog, inLandingPage);
    }
    @GetMapping("getBlogToLandingPage")
    public List<Blog> getBlogToLandingPage() {
        return blogIService.getBlogToLandingPage();
    }
    }
