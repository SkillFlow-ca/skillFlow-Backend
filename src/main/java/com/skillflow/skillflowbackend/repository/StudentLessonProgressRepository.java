package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.StudentLessonProgress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentLessonProgressRepository extends CrudRepository<StudentLessonProgress,Long> {
  @Query("select sp from StudentLessonProgress sp,User u,Lesson l where sp.user.idUser=:userId and sp.lesson.idLesson=:lessonId")
  public Optional<StudentLessonProgress> findProgessLessonByLessonIdAndUserId(@Param("lessonId") long lessonId, @Param("userId") long userId);
  @Query("SELECT COUNT(sp) FROM StudentLessonProgress sp WHERE sp.lesson.module.course.idCourse = :courseId AND sp.user.idUser = :userId AND sp.completed = true")
  long countCompletedLessonsInCourse(@Param("courseId") Long courseId, @Param("userId") Long userId);

}
