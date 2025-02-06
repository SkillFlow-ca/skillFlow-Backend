package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.isDeleted = false and c.admin.idUser=:v1 order by c.createdAt desc")
    public List<Course> getMyCourses(@Param("v1") Long idUser);
    @Query("SELECT c FROM Course c WHERE c.isDeleted = false  ORDER BY c.createdAt DESC")
    Page<Course> findAll(Pageable pageable);
    @Query("SELECT c FROM Course c WHERE c.isDeleted = false and c.courseStatus='PUBLISHED'  ORDER BY c.createdAt DESC")
    Page<Course> findPublishAll(Pageable pageable);

    @Query("SELECT distinct c FROM Course c JOIN c.courseCategoryList cat WHERE c.isDeleted = false and cat.name = :categoryName ORDER BY c.createdAt DESC")
    List<Course> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT distinct count (c) FROM Course c WHERE c.isDeleted = false and c.courseStatus='PUBLISHED'")
    Long  findAllPublishedCourses();
    @Query("SELECT distinct count (c) FROM Course c WHERE c.isDeleted = false and c.courseStatus='DRAFT'")
    Long  findAllDraftCourses();

    @Query("SELECT distinct sum (c.discountPrice) FROM Course c ,Enrollment e where c.free=false and c.idCourse=e.course.idCourse")
    Optional<Double> findAllEarningsFromCourses();


    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.isDeleted = false and c.createdAt >= :currentTimestamp ")
    long countCoursesToday(@Param("currentTimestamp") Instant currentTimestamp);
    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.isDeleted = false and c.isPublished = true AND c.createdAt >= :currentTimestamp")
    long countPublishedCoursesToday(@Param("currentTimestamp") Instant currentTimestamp);

    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.isDeleted = false and c.isPublished = false AND c.createdAt >= :currentTimestamp")
    long countNonPublishedCoursesToday(@Param("currentTimestamp") Instant currentTimestamp);
    @Query("SELECT distinct SUM(c.discountPrice) FROM Course c JOIN Enrollment e ON c.idCourse = e.course.idCourse WHERE c.free = false AND c.isDeleted = false and c.createdAt >= :currentTimestamp")
    Optional<Double> findEarningsFromCoursesToday(Instant currentTimestamp);
    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.admin.idUser = :instructorId AND c.isDeleted = false and c.createdAt >= :currentTimestamp")
    long countCoursesByInstructorToday(@Param("instructorId") Long instructorId, @Param("currentTimestamp") Instant currentTimestamp);

    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.admin.idUser = :instructorId AND  c.courseStatus='PUBLISHED' AND c.isDeleted = false and c.createdAt >= :currentTimestamp")
    long countPublishedCoursesByInstructorToday(@Param("instructorId") Long instructorId, @Param("currentTimestamp") Instant currentTimestamp);

    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.admin.idUser = :instructorId AND  c.courseStatus='DRAFT' AND c.isDeleted = false and  c.createdAt >= :currentTimestamp")
    long countNonPublishedCoursesByInstructorToday(@Param("instructorId") Long instructorId, @Param("currentTimestamp") Instant currentTimestamp);

    @Query("SELECT distinct SUM(c.discountPrice) FROM Course c JOIN Enrollment e ON c.idCourse = e.course.idCourse WHERE c.admin.idUser = :instructorId AND c.isDeleted = false and  c.free = false AND c.createdAt >= :currentTimestamp")
    Optional<Double> findEarningsFromCoursesByInstructorToday(@Param("instructorId") Long instructorId, @Param("currentTimestamp") Instant currentTimestamp);
    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.isDeleted = false and c.admin.idUser = :instructorId")
    long countCoursesByInstructor(@Param("instructorId") Long instructorId);

    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.isDeleted = false and c.admin.idUser = :instructorId AND  c.courseStatus='PUBLISHED'")
    long countPublishedCoursesByInstructor(@Param("instructorId") Long instructorId);

    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.isDeleted = false and c.admin.idUser = :instructorId AND  c.courseStatus='DRAFT'")
    long countNonPublishedCoursesByInstructor(@Param("instructorId") Long instructorId);

    @Query("SELECT distinct SUM(c.discountPrice) FROM Course c JOIN Enrollment e ON c.idCourse = e.course.idCourse WHERE c.admin.idUser = :instructorId AND c.isDeleted = false and c.free = false")
    Optional<Double> findEarningsFromCoursesByInstructor(@Param("instructorId") Long instructorId);

    @Query("SELECT distinct COUNT(c) FROM Course c WHERE c.isDeleted = false and  c.inLandingPage=true ")
    long countCoursesInLandingPage();

    @Query("SELECT distinct c FROM Course c WHERE c.isDeleted = false and c.inLandingPage=true")
    List<Course> findCoursesInLandingPage();
    @Query("SELECT distinct c FROM Course c,Module m,Lesson l WHERE l.idLesson=:idLesson and l.module.idModule=m.idModule and m.course.idCourse=c.idCourse")
    public Course findCourseByLesson(long idLesson);


}
