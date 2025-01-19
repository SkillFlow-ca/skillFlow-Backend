package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Enrollment;
import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.StatusENR;
import com.skillflow.skillflowbackend.repository.EnrollmentRepository;
import com.skillflow.skillflowbackend.service.EnrollmentIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService implements EnrollmentIService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private SessionService sessionService;

    @Override
    public ResponseModel<Enrollment> getEnrollmentsByUserId(Pageable pageable) {
        User user = sessionService.getUserBySession().get();
        Page<Enrollment> jobs = enrollmentRepository.getEnrollmentsByUserId(user.getIdUser(),pageable);
        return buildResponse(jobs);
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

    @Override
    public int countEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.countEnrollmentsByCourseId(courseId);
    }

    private ResponseModel<Enrollment> buildResponse(Page<Enrollment> enrollments) {
        List<Enrollment> listEnrollment = enrollments.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<Enrollment>builder()
                .pageNo(enrollments.getNumber())
                .pageSize(enrollments.getSize())
                .totalElements(enrollments.getTotalElements())
                .totalPages(enrollments.getTotalPages())
                .data(listEnrollment)
                .isLastPage(enrollments.isLast())
                .build();
    }
}
