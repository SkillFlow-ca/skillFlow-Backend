package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.CourseCategory;
import com.skillflow.skillflowbackend.service.CourseCategoryIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/courseCategory/")
@Validated
public class CourseCategoryController {
    @Autowired
    private CourseCategoryIService courseCategoryService;

    @PostMapping("add")
    public CourseCategory saveCourseCategory(@RequestBody CourseCategory courseCategory) {
        return courseCategoryService.save(courseCategory);
    }

    @GetMapping("getAll")
    public ResponseModel<CourseCategory> getAllCourseCategories( @RequestParam(required = false,defaultValue="1")int pageNo,
                                                                 @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<CourseCategory> courseCategoryResponseModel = courseCategoryService.findAll(pageRequestData);
        return courseCategoryResponseModel;
    }

    @PutMapping("update")
    public CourseCategory updateCourseCategory(@RequestBody CourseCategory courseCategory, @RequestParam Long id) {
        return courseCategoryService.updCourseCategory(courseCategory, id);
    }

    @DeleteMapping("delete")
    public void deleteCourseCategory(@RequestParam Long id) {
        courseCategoryService.delCourseCategory(id);
    }

    @GetMapping("getAllNotDeleted")
    public List<CourseCategory> getAllNotDeleted() {
        return courseCategoryService.findAllCategoryNotDeleted();
    }
}
