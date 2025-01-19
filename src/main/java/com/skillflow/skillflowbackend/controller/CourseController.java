package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.CourseDTO;
import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Module;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import com.skillflow.skillflowbackend.service.CourseIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/course/")
@Validated
public class CourseController {
    @Autowired
    private CourseIService courseIService;

    @PostMapping(value = "/add")
    public ResponseEntity<Course> addCourse(@Validated @RequestBody CourseDTO course) {
            Course course1 = courseIService.addCourse(course);
            return ResponseEntity.ok(course1);
    }
    @PostMapping("uploadvideo")
    public ResponseEntity<String> uploadCourseVideo(@RequestParam("courseId") Long courseId, MultipartFile videoFile) {
        try {
            courseIService.uploadCourseVideoAsync(courseId, videoFile); // Asynchronous upload
            return ResponseEntity.accepted().body("Upload started successfully. The video will be processed shortly.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed.");
        }
    }
    @PostMapping("uploadThumbnail")
    public Course uploadThumbnail(@RequestParam("courseId") Long courseId,MultipartFile thumbnail) {
        return courseIService.uploadThumbnail(courseId, thumbnail);
    }
    @DeleteMapping("delete")
    public void deleteDef(@RequestParam("courseId") Long courseId) {
        courseIService.deleteDef(courseId);
    }

    @GetMapping("GetCourse")
        public Course getCourse(@RequestParam("courseId") Long courseId) {
        return courseIService.getCourse(courseId);
    }
    @GetMapping("GetMyCourses")
    public List<Course> getAllMyCourses() {
        return courseIService.getAllMyCourses();
    }
    @PutMapping("updateStatus")
    public void updateStatusOfCourse(@RequestParam("courseId") Long courseId, @RequestParam("status") String status) {
        courseIService.updateStatusOfCourse(courseId, status);
    }
    @GetMapping("getAllCourses")
    public ResponseModel<Course> getAllCourse(@RequestParam(required = false,defaultValue="1")int pageNo,
                                          @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<Course> course= courseIService.getAllCourses(pageRequestData);
        return course;
    }
    @GetMapping("getPublishedCourses")
    public ResponseModel<Course> getAllPublishCourse(@RequestParam(required = false,defaultValue="1")int pageNo,
                                              @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<Course> course= courseIService.getPublishedCourses(pageRequestData);
        return course;
    }
    @PutMapping("Delete")
    public void DeleteCourse(@RequestParam long courseID){
        courseIService.DeleteCourse(courseID);
    }

    @GetMapping("GetCourseByCategoryName")
    public List<Course> getCourseByCategoryName(@RequestParam String categoryName) {
        return courseIService.getCourseByCategoryName(categoryName);
    }
    @PutMapping("updateCourse")
    public Course updateCourse(@Validated @RequestBody CourseDTO course, @RequestParam Long courseId) {
        return courseIService.updateCourse(course, courseId);
    }
    @GetMapping("/getStatisticsAdminInstructor")
    public ResponseEntity<Map<String, Long>> getStatisticsInstructor() {
        return courseIService.getStatisticsCourses();
    }
    @GetMapping("/getStatisticsCoursesInstructor")
    public ResponseEntity<Map<String, Long>> getStatisticsCoursesInstructor() {
        return courseIService.getStatisticsCoursesInstructor();
    }
}
