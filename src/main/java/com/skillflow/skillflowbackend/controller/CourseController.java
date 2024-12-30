package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Module;
import com.skillflow.skillflowbackend.service.CourseIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/course/")
@Validated
public class CourseController {
    @Autowired
    private CourseIService courseIService;

    @PostMapping("add")
    public Course addCourse(@RequestBody Course course) {
        return courseIService.addCourse(course);
    }
    @PostMapping("uploadvideo")
    public Course uploadCourseVideo(@RequestParam("courseId") Long courseId,MultipartFile videoFile) {
        return courseIService.uploadCourseVideo(courseId, videoFile);
    }
    @PostMapping("uploadThumbnail")
    public Course uploadThumbnail(@RequestParam("courseId") Long courseId,MultipartFile thumbnail) {
        return courseIService.uploadThumbnail(courseId, thumbnail);
    }
    @DeleteMapping("delete")
    public void deleteDef(@RequestParam("courseId") Long courseId) {
        courseIService.deleteDef(courseId);
    }
}
