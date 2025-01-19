package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.model.Lesson;
import com.skillflow.skillflowbackend.repository.LessonRepository;
import com.skillflow.skillflowbackend.service.LessonIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/lesson/")
public class LessonController {
    @Autowired
    private LessonIService lessonIService;
    @Autowired
    private LessonRepository lessonRepository;

    @PutMapping("updateLessonToAddVideoFile")
    public ResponseEntity<String> updateLessonToAddVideoFile(@RequestParam Map<String, MultipartFile> fileLessons) {
        try {
            lessonIService.updateLessonToAddVideoFileAsync(fileLessons);
            return ResponseEntity.ok("Lesson video files updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating lesson video files. Please try again.");
        }
    }
    @PutMapping("uploadvideo")
    public ResponseEntity<String> uploadLessonVideo(@RequestParam Map<String, MultipartFile> fileLessons) {
        try {
            List<Lesson> updatedLessons = lessonIService.updateLessonToAddVideoFile(fileLessons);
            return ResponseEntity.ok("Upload successful for " + updatedLessons.size() + " lessons.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed.");
        }
    }
    @GetMapping("getLesson")
    public Lesson getLesson() {
        return lessonRepository.findById(30L).get();
    }
    @DeleteMapping("delete")
    public void deleteLesson(@RequestParam("lessonId") Long lessonId) {
        lessonIService.deleteLesson(lessonId);
    }
}
