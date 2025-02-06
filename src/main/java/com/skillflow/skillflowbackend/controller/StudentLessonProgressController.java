package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.service.StudentLessonProgressIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/studentLessonProgress/")
@Validated
public class StudentLessonProgressController {
    @Autowired
    private StudentLessonProgressIService studentLessonProgressIService;

    @PutMapping("/updateProgress")
    public void updateProgress(@RequestParam Long lessonId,@RequestParam double progress) {
        studentLessonProgressIService.updateProgress(lessonId, progress);
    }
}
