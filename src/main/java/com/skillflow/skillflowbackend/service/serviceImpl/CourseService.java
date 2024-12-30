package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Lesson;
import com.skillflow.skillflowbackend.model.Module;
import com.skillflow.skillflowbackend.repository.CourseRepository;
import com.skillflow.skillflowbackend.repository.LessonRepository;
import com.skillflow.skillflowbackend.repository.ModuleRepository;
import com.skillflow.skillflowbackend.service.CourseIService;
import com.skillflow.skillflowbackend.utility.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

@Service
public class CourseService implements CourseIService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private S3Service s3Service;
    @Transactional
    @Override
    public Course addCourse(Course course) {
        course.setCreatedAt(Instant.now());
        Course couse1=courseRepository.save(course);
        for (Module module1 : course.getModuleList()) {
            Module module = new Module();
            module.setName(module1.getName());
            module.setCourse(couse1);
            module.setCreatedAt(Instant.now());
            Module savedModule =moduleRepository.save(module);
            // Ensure lesson list is not null
            if (module1.getLessonList() == null) {
                module1.setLessonList(new ArrayList<>());
            }

            for(Lesson l:module1.getLessonList()){
                Lesson lesson=new Lesson();
                lesson.setCreatedAt(Instant.now());
                lesson.setTitle(l.getTitle());
                lesson.setContent(l.getContent());
                lesson.setModule(savedModule);
                System.out.println(l.getVideoFile());
                // Handle video file upload for lesson
                if (l.getVideoFile() != null) {
                    try {
                        File tempFile = File.createTempFile("upload-", l.getVideoFile().getOriginalFilename());
                        l.getVideoFile().transferTo(tempFile);
                        String videoUrl = s3Service.uploadFile(tempFile, "videosLesson/" + l.getVideoFile().getOriginalFilename());
                        tempFile.delete();
                        lesson.setUrlvideoLesson(videoUrl);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload lesson video", e);
                    }
                }
                lessonRepository.save(lesson);
            }
        }
        return couse1;
    }

    @Override
    public Course uploadCourseVideo(Long courseId, MultipartFile videoFile) {
        try {
            // Save the video to a temporary file
            File tempFile = File.createTempFile("upload-", videoFile.getOriginalFilename());
            videoFile.transferTo(tempFile);

            // Upload to S3 and get the video URL
            String videoUrl = s3Service.uploadFile(tempFile, "introVideos/" + videoFile.getOriginalFilename());

            // Delete the temporary file
            tempFile.delete();

            // Save the video URL in the course entity
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
            course.setIntroCourse(videoUrl);

            return courseRepository.save(course);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload video", e);
        }
    }

    @Override
    public Course uploadThumbnail(Long courseId, MultipartFile thumbnail) {
        try {
            // Save the video to a temporary file
            File tempFile = File.createTempFile("upload-", thumbnail.getOriginalFilename());
            thumbnail.transferTo(tempFile);

            // Upload to S3 and get the video URL
            String thumbUrl = s3Service.uploadFile(tempFile, "thumbnail/" + thumbnail.getOriginalFilename());

            // Delete the temporary file
            tempFile.delete();

            // Save the video URL in the course entity
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
            course.setThumbnailUrl(thumbUrl);

            return courseRepository.save(course);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload video", e);
        }
    }

    @Override
    public void deleteDef(Long courseId) {
        courseRepository.deleteById(courseId);
    }

}
