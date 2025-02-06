package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Lesson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {
    @Query("SELECT l FROM Lesson l WHERE l.reference = :reference")
    Lesson findByReference(@Param("reference") String reference);

    public List<Lesson> findLessonByModule_IdModule(Long moduleId);


    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.module.course.idCourse = :courseId")
    long countAllLessonsInCourse(@Param("courseId") Long courseId);
}