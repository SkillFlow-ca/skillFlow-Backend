package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.CourseDTO;
import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CourseIService {
   public Course addCourse(CourseDTO course);
   public Course uploadCourseVideo(Long courseId, MultipartFile videoFile) ;
   public void uploadCourseVideoAsync(Long courseId, MultipartFile videoFile);
   public void updateStatusOfCourse(Long courseId, String status);

   public Course uploadThumbnail(Long courseId, MultipartFile thumbnail);
   ResponseModel<Course> getAllCourses(Pageable pageable);
   public List<Course> getAllMyCourses();

   public Course getCourse(Long courseId);
   public void deleteDef(Long courseId);
   public void DeleteCourse(Long courseId);
   public List<Course> getCourseByCategoryName(String categoryName);
}
