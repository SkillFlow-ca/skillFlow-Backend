package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Enrollment;
import com.skillflow.skillflowbackend.model.Job;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnrollmentIService {
    public ResponseModel<Enrollment> getEnrollmentsByUserId(Pageable pageable);
    public List<Enrollment> addEnrollmentAndAssignToCourse(Enrollment enrollment, List<Course> courseList);
    public int countEnrollmentsByCourseId(Long courseId);
}
