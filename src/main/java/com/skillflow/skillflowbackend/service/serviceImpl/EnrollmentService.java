package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Enrollment;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.StatusENR;
import com.skillflow.skillflowbackend.repository.EnrollmentRepository;
import com.skillflow.skillflowbackend.service.EnrollmentIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentService implements EnrollmentIService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private SessionService sessionService;

    @Override
    public List<Enrollment> getEnrollmentsByUserId() {
        User user = sessionService.getUserBySession().get();
        return enrollmentRepository.getEnrollmentsByUserId(user.getIdUser());
    }

    @Override
    public List<Enrollment> addEnrollmentAndAssignToCourse(Enrollment enrollment, List<Course> courseList){
        List<Enrollment> enrollments = new ArrayList<>();
        User user = sessionService.getUserBySession().get();
        enrollment.setUser(user);
        enrollment.setStatusEnr(StatusENR.ACTIVE);
        enrollment.setEnrollmentDate(Instant.now());
        enrollment.setCreatedAt(Instant.now());
        for (Course c: courseList) {
            enrollment.setCourse(c);
            enrollments.add(enrollment);
            enrollmentRepository.save(enrollment);
        }
        return enrollments;
    }
}
