package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dao.mapper.CourseMapper;
import com.skillflow.skillflowbackend.dto.CourseDTO;
import com.skillflow.skillflowbackend.dto.LessonDTO;
import com.skillflow.skillflowbackend.dto.ModuleDTO;
import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Lesson;
import com.skillflow.skillflowbackend.model.Module;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.CourseStatus;
import com.skillflow.skillflowbackend.model.enume.TypeLesson;
import com.skillflow.skillflowbackend.repository.CourseRepository;
import com.skillflow.skillflowbackend.repository.LessonRepository;
import com.skillflow.skillflowbackend.repository.ModuleRepository;
import com.skillflow.skillflowbackend.service.CourseIService;
import com.skillflow.skillflowbackend.utility.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CourseService implements CourseIService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseMapper mapToCourse;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private SessionService sessionService;
    @Transactional
    @Override
    public Course addCourse(CourseDTO courseDTO) {
        Course course = mapToCourse.mapToCourse(courseDTO);
        course.setCreatedAt(Instant.now());
        course.setReference(UUID.randomUUID().toString());
        course.setCourseStatus(CourseStatus.DRAFT);
        course.setAdmin(sessionService.getUserBySession().get());
        course.setIsDeleted(false);
        Course savedCourse = courseRepository.save(course);
        if (courseDTO.getModuleList() == null) {
            courseDTO.setModuleList(new ArrayList<>());
        }
        for (ModuleDTO module1 : courseDTO.getModuleList()) {
            Module module = new Module();
            module.setName(module1.getName());
            module.setCourse(savedCourse);
            module.setCreatedAt(Instant.now());
            Module savedModule = moduleRepository.save(module);
            if (module1.getLessonList() == null) {
                module1.setLessonList(new ArrayList<>());
            }
            for (LessonDTO l : module1.getLessonList()) {
                Lesson lesson = new Lesson();
                lesson.setCreatedAt(Instant.now());
                if(l.getTypeLesson().equals(TypeLesson.MARKDOWN)){
                    lesson.setTitle(l.getTitle());
                    lesson.setContent(l.getContent());
                    lesson.setUrlvideoLesson(null);
                    lesson.setTypeLesson(TypeLesson.MARKDOWN);
                } else if (l.getTypeLesson().equals(TypeLesson.PDF)){
                    lesson.setTitlePdf(l.getTitle());
                    lesson.setUrlvideoLesson(null);
                    lesson.setTypeLesson(TypeLesson.PDF);
                }
                else {
                    lesson.setTitleVideo(l.getTitleVideo());
                    lesson.setTypeLesson(TypeLesson.VIDEO);
                    lesson.setDuration(l.getDuration());
                }
                lesson.setModule(savedModule);
                lesson.setReference(l.getReference());
                lessonRepository.save(lesson);
            }
        }
        return savedCourse;
    }

    @Override
    public Course uploadCourseVideo(Long courseId, MultipartFile videoFile) {
        uploadCourseVideoAsync(courseId, videoFile);
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }

    @Override
    @Async
    public void uploadCourseVideoAsync(Long courseId, MultipartFile videoFile) {
        try (InputStream inputStream = videoFile.getInputStream()) {
            String videoUrl = s3Service.uploadStream(inputStream, "introVideos/" + videoFile.getOriginalFilename());

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
            course.setIntroCourse(videoUrl);
            courseRepository.save(course);
        } catch (IOException e) {
            throw new RuntimeException("Async video upload failed", e);
        }
    }

    @Override
    public void updateStatusOfCourse(Long courseId, String status) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        course.setCourseStatus(CourseStatus.valueOf(status));
        courseRepository.save(course);
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
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public List<Course> getAllMyCourses() {
        User user=sessionService.getUserBySession().get();
        List<Course> courseList=courseRepository.getMyCourses(user.getIdUser());
        return courseList;
    }

    @Override
    public Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }

    @Override
    public void deleteDef(Long courseId) {
        courseRepository.deleteById(courseId);
    }

}
