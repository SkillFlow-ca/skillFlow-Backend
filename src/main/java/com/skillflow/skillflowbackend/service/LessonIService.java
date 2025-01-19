package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Lesson;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface LessonIService {
    public List<Lesson> updateLessonToAddVideoFile(Map<String, MultipartFile> fileLessons) ;

    public void updateLessonToAddVideoFileAsync(Map<String, MultipartFile> fileLessons);
    public void deleteLesson(Long idLesson);
}
