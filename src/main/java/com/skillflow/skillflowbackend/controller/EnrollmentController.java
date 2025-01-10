package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.EnrollmentRequest;
import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Enrollment;
import com.skillflow.skillflowbackend.service.EnrollmentIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/enrollment/")
@Validated
public class EnrollmentController {
    @Autowired
    private EnrollmentIService enrollmentIService;

    @GetMapping("GetEnrollmentsByUserId")
    public List<Enrollment> getEnrollmentsByUserId() {
        return enrollmentIService.getEnrollmentsByUserId();
    }
    @PostMapping("addEnrollmentAndAssignToCourse")
    public List<Enrollment> addEnrollmentAndAssignToCourse(@RequestBody EnrollmentRequest enrollmentRequest) {
        return enrollmentIService.addEnrollmentAndAssignToCourse(enrollmentRequest.getEnrollment(), enrollmentRequest.getCourseList());
    }

}
