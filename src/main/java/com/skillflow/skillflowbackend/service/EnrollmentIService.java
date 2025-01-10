package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Enrollment;

import java.util.List;

public interface EnrollmentIService {
    public List<Enrollment> getEnrollmentsByUserId();
    public List<Enrollment> addEnrollmentAndAssignToCourse(Enrollment enrollment, List<Course> courseList);
}
