package com.skillflow.skillflowbackend.dto;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Enrollment;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EnrollmentRequest {
    private Enrollment enrollment;
    private List<Course> courseList;
}
