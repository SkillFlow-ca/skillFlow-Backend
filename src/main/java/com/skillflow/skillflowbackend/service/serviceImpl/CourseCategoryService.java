package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.CourseCategory;
import com.skillflow.skillflowbackend.repository.CourseCategoryRepository;
import com.skillflow.skillflowbackend.service.CourseCategoryIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseCategoryService implements CourseCategoryIService {
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;
    @Override
    public CourseCategory save(CourseCategory courseCategory) {
        courseCategory.setCreatedAt(Instant.now());
        courseCategory.setIsDeleted(false);
        return courseCategoryRepository.save(courseCategory);
    }

    @Override
    public ResponseModel<CourseCategory>  findAll(Pageable pageable) {
        Page<CourseCategory> courseCategory = courseCategoryRepository.findByIsDeletedFalse(pageable);
        return buildResponse(courseCategory);
    }

    @Override
    public CourseCategory updCourseCategory(CourseCategory courseCategory, Long idCourseCategory) {
        CourseCategory courseCategory1= courseCategoryRepository.findById(idCourseCategory).get();
        courseCategory.setCourseList(courseCategory1.getCourseList());
        courseCategory.setCreatedAt(courseCategory1.getCreatedAt());
        courseCategory.setIsDeleted(courseCategory1.getIsDeleted());
        courseCategory.setUpdatedAt(Instant.now());
        courseCategory.setDescription(courseCategory.getDescription());
        courseCategory.setName(courseCategory.getName());
        return courseCategoryRepository.save(courseCategory);
    }

    @Override
    public void delCourseCategory(Long idCourseCategory) {
        CourseCategory courseCategory= courseCategoryRepository.findById(idCourseCategory).get();
        courseCategory.setIsDeleted(true);
        courseCategoryRepository.save(courseCategory);
    }
    private ResponseModel<CourseCategory> buildResponse(Page<CourseCategory> courseCategory) {
        List<CourseCategory> listCourseCategory = courseCategory.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<CourseCategory>builder()
                .pageNo(courseCategory.getNumber())
                .pageSize(courseCategory.getSize())
                .totalElements(courseCategory.getTotalElements())
                .totalPages(courseCategory.getTotalPages())
                .data(listCourseCategory)
                .isLastPage(courseCategory.isLast())
                .build();
    }
}
