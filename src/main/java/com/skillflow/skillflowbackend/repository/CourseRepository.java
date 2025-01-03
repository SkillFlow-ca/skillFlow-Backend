package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.isDeleted = false and c.admin.idUser=:v1 order by c.createdAt desc")
    public List<Course> getMyCourses(@Param("v1") Long idUser);
}
