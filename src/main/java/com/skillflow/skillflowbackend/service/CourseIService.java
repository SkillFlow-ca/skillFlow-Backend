package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.CourseDTO;
import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface CourseIService {
   public Course addCourse(CourseDTO course);
   public Course updateCourse(CourseDTO course,long idCourse);
   public Course uploadCourseVideo(Long courseId, MultipartFile videoFile) ;
   public void uploadCourseVideoAsync(Long courseId, MultipartFile videoFile);
   public void updateStatusOfCourse(Long courseId, String status);
   public void updateInLandingPage(Long courseId, boolean inLandingPage);
   public List<Course> getCoursesForLandingPage();

   public ResponseEntity<Map<String, Long>> getStatisticsCourses();
   public ResponseEntity<Map<String, Long>> getStatisticsCoursesInstructor();
   public Course uploadThumbnail(Long courseId, MultipartFile thumbnail);
   ResponseModel<Course> getAllCourses(Pageable pageable);
   ResponseModel<Course> getPublishedCourses(Pageable pageable);
   public List<Course> getAllMyCourses();

   public Course getCourse(Long courseId);
   public void deleteDef(Long courseId);
   public void DeleteCourse(Long courseId);
   public List<Course> getCourseByCategoryName(String categoryName);
}
