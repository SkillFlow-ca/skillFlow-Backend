package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dao.mapper.CourseMapper;
import com.skillflow.skillflowbackend.dto.CourseDTO;
import com.skillflow.skillflowbackend.dto.LessonDTO;
import com.skillflow.skillflowbackend.dto.ModuleDTO;
import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.*;
import com.skillflow.skillflowbackend.model.Module;
import com.skillflow.skillflowbackend.model.enume.CourseStatus;
import com.skillflow.skillflowbackend.model.enume.TypeLesson;
import com.skillflow.skillflowbackend.repository.CourseRepository;
import com.skillflow.skillflowbackend.repository.LessonRepository;
import com.skillflow.skillflowbackend.repository.ModuleRepository;
import com.skillflow.skillflowbackend.service.CourseIService;
import com.skillflow.skillflowbackend.utility.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
                lesson.setReference(UUID.randomUUID().toString());
                if(l.getTypeLesson().equals(TypeLesson.MARKDOWN)){
                    lesson.setTitle(l.getTitle());
                    lesson.setContent(l.getContent());
                    lesson.setUrlvideoLesson(null);
                    lesson.setTypeLesson(TypeLesson.MARKDOWN);
                } else if (l.getTypeLesson().equals(TypeLesson.PDF)){
                    lesson.setTitlePdf(l.getTitlePdf());
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
    @Transactional
    public Course updateCourse(CourseDTO courseDTO, long idCourse) {
        Course existingCourse = courseRepository.findById(idCourse)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + idCourse));

        existingCourse.setTitle(courseDTO.getTitle());
        existingCourse.setLongDescription(courseDTO.getLongDescription());
        existingCourse.setReference(courseDTO.getReference());
        existingCourse.setShortDescription(courseDTO.getShortDescription());
        existingCourse.setCourseCategoryList(courseDTO.getCourseCategoryList());
        existingCourse.setIntroCourse(courseDTO.getIntroCourse());
        existingCourse.setThumbnailUrl(courseDTO.getThumbnailUrl());
        existingCourse.setIsDeleted(courseDTO.getIsDeleted());
        existingCourse.setRequirements(courseDTO.getRequirements());
        existingCourse.setWhatWillStudentLearn(courseDTO.getWhatWillStudentLearn());
        existingCourse.setFree(courseDTO.isFree());
        existingCourse.setAudioLanguage(courseDTO.getAudioLanguage());
        existingCourse.setDiscountPrice(courseDTO.getDiscountPrice());
        existingCourse.setRegularPrice(courseDTO.getRegularPrice());
        existingCourse.setCourseStatus(courseDTO.getCourseStatus());
        existingCourse.setAdmin(sessionService.getUserBySession().get());
        existingCourse.setIntroCourseType(courseDTO.getIntroCourseType());
        existingCourse.setIsPublished(courseDTO.getIsPublished());
        existingCourse.setCreatedAt(courseDTO.getCreatedAt());
        existingCourse.setUpdatedAt(Instant.now());

        // Update modules
        List<Module> existingModules = moduleRepository.findModuleByCourse_IdCourse(existingCourse.getIdCourse());
        for (ModuleDTO moduleDTO : courseDTO.getModuleList()) {
            Module module = existingModules.stream()
                    .filter(m -> m.getIdModule()==(moduleDTO.getIdModule()))
                    .findFirst()
                    .orElse(new Module());
            module.setName(moduleDTO.getName());
            module.setCourse(existingCourse);
            module.setCreatedAt(moduleDTO.getCreatedAt());
            module.setUpdatedAt(Instant.now());
            moduleRepository.save(module);

            // Update lessons
            List<Lesson> existingLessons = lessonRepository.findLessonByModule_IdModule(module.getIdModule());
            for (LessonDTO lessonDTO : moduleDTO.getLessonList()) {
                Lesson lesson = existingLessons.stream()
                        .filter(l -> l.getIdLesson()==(lessonDTO.getIdLesson()))
                        .findFirst()
                        .orElse(new Lesson());
                lesson.setUpdatedAt(Instant.now());
                lesson.setCreatedAt(lesson.getCreatedAt());
                if(lessonDTO.getTypeLesson().equals(TypeLesson.MARKDOWN)){
                    lesson.setTitle(lessonDTO.getTitle());
                    lesson.setContent(lessonDTO.getContent());
                    lesson.setUrlvideoLesson(null);
                    lesson.setTypeLesson(TypeLesson.MARKDOWN);
                    lesson.setDuration("150");
                } else if (lessonDTO.getTypeLesson().equals(TypeLesson.PDF)){
                    lesson.setTitlePdf(lessonDTO.getTitlePdf());
                    lesson.setUrlvideoLesson(null);
                    lesson.setTypeLesson(TypeLesson.PDF);
                    lesson.setDuration("150");
                }
                else {
                    lesson.setTitleVideo(lessonDTO.getTitleVideo());
                    lesson.setTypeLesson(TypeLesson.VIDEO);
                    lesson.setDuration(lessonDTO.getDuration());
                }
                lesson.setModule(module);
                lesson.setReference(lessonDTO.getReference());
                lesson.setUpdatedAt(Instant.now());
                lessonRepository.save(lesson);
            }
        }

        return courseRepository.save(existingCourse);
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
    public void updateInLandingPage(Long courseId, boolean inLandingPage) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        if(inLandingPage==true){
            long count = courseRepository.countCoursesInLandingPage();
            if (count >= 3) {
                throw new RuntimeException("Only three courses can be in the landing page at a time.");
            }
            else{
                course.setInLandingPage(inLandingPage);
                courseRepository.save(course);
            }
        }
        else {
            course.setInLandingPage(inLandingPage);
            courseRepository.save(course);
        }
    }

    @Override
    public List<Course> getCoursesForLandingPage() {
        return courseRepository.findCoursesInLandingPage();
    }

    @Override
    public ResponseEntity<Map<String, Long>> getStatisticsCourses() {
        Map<String, Long> statistics = new HashMap<>();
        statistics.put("totalCourses", countCourses());
        statistics.put("publishedCourses", countPublishedCourses());
        statistics.put("unpublishedCourses", countUnpublishedCourses());
        statistics.put("totalEarning", (long) countTotalEarning());
        statistics.put("coursesToday", countCoursesToday());
        statistics.put("publishedCoursesToday", countcountPublishedCoursesToday());
        statistics.put("nonPublishedCoursesToday", countNonPublishedCoursesToday());
        statistics.put("earningsFromCoursesToday", (long) countEarningsFromCoursesToday());
        return ResponseEntity.ok(statistics);
    }

    @Override
    public ResponseEntity<Map<String, Long>> getStatisticsCoursesInstructor() {
        User instructor = sessionService.getUserBySession().get();
        Map<String, Long> statistics = new HashMap<>();
        statistics.put("totalCourses", countCoursesByInstructor(instructor.getIdUser()));
        statistics.put("publishedCourses", countPublishedCoursesByInstructor(instructor.getIdUser()));
        statistics.put("unpublishedCourses", countNonPublishedCoursesByInstructor(instructor.getIdUser()));
        statistics.put("totalEarning", (long) findEarningsFromCoursesByInstructor(instructor.getIdUser()));
        statistics.put("coursesToday", countCoursesByInstructorToday(instructor.getIdUser()));
        statistics.put("publishedCoursesToday", countPublishedCoursesByInstructorToday(instructor.getIdUser()));
        statistics.put("nonPublishedCoursesToday", countNonPublishedCoursesByInstructorToday(instructor.getIdUser()));
        statistics.put("earningsFromCoursesToday", (long) findEarningsFromCoursesByInstructorToday(instructor.getIdUser()));
        return ResponseEntity.ok(statistics);
    }
    long countCoursesByInstructorToday(Long instructorId) {
        return courseRepository.countCoursesByInstructorToday(instructorId, Instant.now());
    }

    long countPublishedCoursesByInstructorToday(Long instructorId) {
        return courseRepository.countPublishedCoursesByInstructorToday(instructorId, Instant.now());
    }

    long countNonPublishedCoursesByInstructorToday(Long instructorId) {
        return courseRepository.countNonPublishedCoursesByInstructorToday(instructorId, Instant.now());
    }

    double findEarningsFromCoursesByInstructorToday(Long instructorId) {
        return courseRepository.findEarningsFromCoursesByInstructorToday(instructorId, Instant.now()).orElse(0.0);
    }
    long countCoursesByInstructor(Long instructorId) {
        return courseRepository.countCoursesByInstructor(instructorId);
    }

    long countPublishedCoursesByInstructor(Long instructorId) {
        return courseRepository.countPublishedCoursesByInstructor(instructorId);
    }

    long countNonPublishedCoursesByInstructor(Long instructorId) {
        return courseRepository.countNonPublishedCoursesByInstructor(instructorId);
    }
    double findEarningsFromCoursesByInstructor(Long instructorId) {
        return courseRepository.findEarningsFromCoursesByInstructor(instructorId).orElse(0.0);
    }

    long countCoursesToday() {
        return courseRepository.countCoursesToday(Instant.now());
    }
    long countcountPublishedCoursesToday() {
        return courseRepository.countPublishedCoursesToday(Instant.now());
    }
    long countNonPublishedCoursesToday() {
        return courseRepository.countNonPublishedCoursesToday(Instant.now());
    }
    double countEarningsFromCoursesToday() {
        return courseRepository.findEarningsFromCoursesToday(Instant.now()).orElse(0.0);
    }

    long countCourses() {
        return courseRepository.count();
    }
    long countPublishedCourses() {
        return courseRepository.findAllPublishedCourses();
    }
    long countUnpublishedCourses() {
        return courseRepository.findAllDraftCourses();
    }

    double countTotalEarning(){
        return courseRepository.findAllEarningsFromCourses().orElse(0.0);
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
    public ResponseModel<Course> getAllCourses(Pageable pageable) {
        Page<Course> courseList=courseRepository.findAll(pageable);
        return buildResponse(courseList);
    }

    @Override
    public ResponseModel<Course> getPublishedCourses(Pageable pageable) {
        Page<Course> courseList=courseRepository.findPublishAll(pageable);
        return buildResponse(courseList);
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

    @Override
    public void DeleteCourse(Long courseId) {
        Course course=courseRepository.findById(courseId).get();
        course.setIsDeleted(true);
        courseRepository.save(course);
    }

    @Override
    public List<Course> getCourseByCategoryName(String categoryName) {
        return courseRepository.findByCategoryName(categoryName);
    }


    private ResponseModel<Course> buildResponse(Page<Course> course) {
        List<Course> listcourse = course.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<Course>builder()
                .pageNo(course.getNumber())
                .pageSize(course.getSize())
                .totalElements(course.getTotalElements())
                .totalPages(course.getTotalPages())
                .data(listcourse)
                .isLastPage(course.isLast())
                .build();
    }

}
