package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.*;
import com.skillflow.skillflowbackend.repository.CourseRepository;
import com.skillflow.skillflowbackend.repository.EnrollmentRepository;
import com.skillflow.skillflowbackend.repository.LessonRepository;
import com.skillflow.skillflowbackend.repository.StudentLessonProgressRepository;
import com.skillflow.skillflowbackend.service.StudentLessonProgressIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentLessonService implements StudentLessonProgressIService {
    @Autowired
    private StudentLessonProgressRepository studentLessonProgressRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public void updateProgress(Long lessonId, double progress) {
        User user = sessionService.getUserBySession()
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Optional<StudentLessonProgress> optionalProgressEntity = studentLessonProgressRepository
                .findProgessLessonByLessonIdAndUserId(lessonId, user.getIdUser());

        StudentLessonProgress progressEntity;
        if (optionalProgressEntity.isPresent()) {
            progressEntity = optionalProgressEntity.get();
        } else {
            progressEntity = new StudentLessonProgress();
            progressEntity.setLesson(lesson);
            progressEntity.setUser(user);
        }

        if (progress > progressEntity.getProgressPercentage()) {
            progressEntity.setProgressPercentage(progress);
            progressEntity.setCompleted(progress == 100);
            studentLessonProgressRepository.save(progressEntity);
            if(progress==100){
                setProgressToenrollment(lessonId);
            }
        }
    }
    public void setProgressToenrollment(Long lessonId) {
        Course course = courseRepository.findCourseByLesson(lessonId);
        User user = sessionService.getUserBySession()
                .orElseThrow(() -> new RuntimeException("User not found"));
        Enrollment enrollment = enrollmentRepository.getEnrollmentByCourseAndUser(course.getIdCourse(), user.getIdUser());

        double progress = 0;
        if (countAllLessonsInCourse(course.getIdCourse()) != 0) {
            System.out.println(countCompletedLessonsInCourse(course.getIdCourse()));
            System.out.println(countAllLessonsInCourse(course.getIdCourse()));
            progress = (double) countCompletedLessonsInCourse(course.getIdCourse()) / countAllLessonsInCourse(course.getIdCourse()) * 100;
            enrollment.setProgress(progress);
            enrollmentRepository.save(enrollment);
        }
    }

    public long countCompletedLessonsInCourse(Long courseId) {
        User user = sessionService.getUserBySession()
                .orElseThrow(() -> new RuntimeException("User not found"));
        return studentLessonProgressRepository.countCompletedLessonsInCourse(courseId, user.getIdUser());
    }

    public long countAllLessonsInCourse(Long courseId) {
        return lessonRepository.countAllLessonsInCourse(courseId);
    }
}
