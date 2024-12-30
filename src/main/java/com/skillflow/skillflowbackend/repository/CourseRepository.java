package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
}
