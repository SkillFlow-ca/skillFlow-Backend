package com.skillflow.skillflowbackend.dao.mapper;

import com.skillflow.skillflowbackend.dto.BlogAddDTO;
import com.skillflow.skillflowbackend.dto.BlogDTO;
import com.skillflow.skillflowbackend.dto.UserDTO;
import com.skillflow.skillflowbackend.dto.UserRegisterDTO;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.User;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BlogMapper {
    @Autowired
    private ModelMapper modelMapper;

    public BlogDTO mapToBlogDto(Blog blog) {
        return modelMapper.map(blog, BlogDTO.class);
    }
    public Blog mapToBlog(BlogAddDTO blogAddDTO) {
        return modelMapper.map(blogAddDTO, Blog.class);
    }

}
