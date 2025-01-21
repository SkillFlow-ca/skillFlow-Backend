package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BlogIService {
    public List<Blog> create3BlogWitAI();
    public Blog getBlogById(long  idBlog);
    ResponseModel<Blog> getBlogsByStatusOrderByCreatedAt(StatusBlog status, Pageable pageable);

    public Blog changeStatusOfBlog(long idBlog,StatusBlog statusBlog);
    public Blog updateBlog(long id, Blog blog);
    public Blog updateBlogToAddImage(long idBlog,MultipartFile img) throws IOException;

    public void deleteBlog(long id);

    public List<Blog> getBlogToLandingPage();
    public void updateBlogInLandingPage(long idBlog, boolean inLandingPage);


}
