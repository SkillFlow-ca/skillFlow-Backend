package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.CourseCategory;
import org.springframework.data.domain.Pageable;

public interface CourseCategoryIService {
    public CourseCategory save(CourseCategory courseCategory);
    public ResponseModel<CourseCategory>  findAll(Pageable pageable);
    public CourseCategory updCourseCategory(CourseCategory courseCategory, Long idCourseCategory);
    public void delCourseCategory(Long idCourseCategory);
}
