package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.exception.UserServiceCustomException;
import com.skillflow.skillflowbackend.model.Lesson;
import com.skillflow.skillflowbackend.repository.LessonRepository;
import com.skillflow.skillflowbackend.service.LessonIService;
import com.skillflow.skillflowbackend.utility.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LessonService implements LessonIService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private S3Service s3Service;

    @Override
    public List<Lesson> updateLessonToAddVideoFile(Map<String, MultipartFile> fileLessons) {
        updateLessonToAddVideoFileAsync(fileLessons);
        String lessonRef=null;
        List<Lesson> lessonList=new ArrayList<>();
        for (Map.Entry<String, MultipartFile> entry : fileLessons.entrySet()) {
             lessonRef = entry.getKey();
             Lesson lesson=lessonRepository.findByReference(lessonRef);
             lessonList.add(lesson);
        }
        return lessonList;
    }

    @Override
    @Async
    @Transactional
    public void updateLessonToAddVideoFileAsync(Map<String, MultipartFile> fileLessons) {
        try {
            for (Map.Entry<String, MultipartFile> entry : fileLessons.entrySet()) {
                String lessonRef = entry.getKey();
                System.out.println("Processing lesson reference: " + lessonRef);

                // Retrieve lesson from the database
                Lesson lesson = lessonRepository.findByReference(lessonRef);
                if (lesson == null) {
                    System.err.println("Lesson not found for reference: " + lessonRef);
                    continue; // Skip this entry and process the next one
                }

                // Process only if titleVideo is not null
                if (lesson.getTitleVideo() != null) {
                    MultipartFile fileLesson = entry.getValue();
                    try (InputStream inputStream = fileLesson.getInputStream()) {
                        String videoUrl = s3Service.uploadStream(inputStream, "videosLesson/" + fileLesson.getOriginalFilename());
                        lesson.setUrlvideoLesson(videoUrl);
                        lessonRepository.save(lesson);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload video for lesson", e);
                    }
                } else {
                    System.out.println("Skipping lesson with no titleVideo for reference: " + lessonRef);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserServiceCustomException(
                    "Error updating lesson video files",
                    "An error occurred while updating lesson video files. Please try again.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    public Lesson getLesson() {
        System.out.println("Fetching lesson with ID: 30");
        try {
            return lessonRepository.findById(30L)
                    .orElseThrow(() -> new RuntimeException("Lesson not found with ID: 30"));
        } catch (Exception e) {
            System.err.println("Error fetching lesson: " + e.getMessage());
            throw e;
        }
    }

}