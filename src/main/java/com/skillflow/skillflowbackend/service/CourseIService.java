package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Module;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseIService {
   public Course addCourse(Course course);
   public Course uploadCourseVideo(Long courseId, MultipartFile videoFile) ;

   public Course uploadThumbnail(Long courseId, MultipartFile thumbnail);
   public void deleteDef(Long courseId);
}
