package com.skillflow.skillflowbackend.dao.mapper;

import com.skillflow.skillflowbackend.dto.BlogAddDTO;
import com.skillflow.skillflowbackend.dto.CourseDTO;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.Course;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Course mapToCourse(CourseDTO courseDTO) {
        return modelMapper.map(courseDTO, Course.class);
    }

}
