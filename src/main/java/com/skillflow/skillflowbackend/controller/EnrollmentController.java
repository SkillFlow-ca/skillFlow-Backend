package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.EnrollmentRequest;
import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Enrollment;
import com.skillflow.skillflowbackend.model.PaymentSkillFlow;
import com.skillflow.skillflowbackend.service.EnrollmentIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseModel<Enrollment> getEnrollmentsByUserId(@RequestParam(required = false,defaultValue="1")int pageNo,
                                                            @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<Enrollment> payments = enrollmentIService.getEnrollmentsByUserId(pageRequestData);
        return payments;
    }
    @PostMapping("addEnrollmentAndAssignToCourse")
    public List<Enrollment> addEnrollmentAndAssignToCourse(@RequestBody EnrollmentRequest enrollmentRequest) {
        return enrollmentIService.addEnrollmentAndAssignToCourse(enrollmentRequest.getEnrollment(), enrollmentRequest.getCourseList());
    }
    @GetMapping("countEnrollmentsByCourseId")
    public int countEnrollmentsByCourseId(@RequestParam("courseId") Long courseId) {
        return enrollmentIService.countEnrollmentsByCourseId(courseId);
    }

}
