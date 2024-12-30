package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Lesson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {
}
