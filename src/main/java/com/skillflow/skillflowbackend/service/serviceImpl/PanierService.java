package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.Course;
import com.skillflow.skillflowbackend.model.Panier;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.StatusPanier;
import com.skillflow.skillflowbackend.repository.CourseRepository;
import com.skillflow.skillflowbackend.repository.PanierRepository;
import com.skillflow.skillflowbackend.service.PanierIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class PanierService implements PanierIService {
    @Autowired
    private PanierRepository panierRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SessionService sessionService;


    @Override
    public Panier addCourseToPanier(Long courseId, Panier panier) {
        // Step 1: Get the current user from the session
        User user = sessionService.getUserBySession().get();

        // Step 2: Check if the course exists
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (!optionalCourse.isPresent()) {
            throw new RuntimeException("Course with ID " + courseId + " not found.");
        }

        Course course = optionalCourse.get();

        // Step 3: Check if the user already has a PENDING panier
        Panier existingPanier = panierRepository.findByUserAndStatusPanier(user, StatusPanier.PENDING);

        // If an existing PENDING panier is found, use it; otherwise, create a new one
        if (existingPanier == null) {
            existingPanier = new Panier();
            existingPanier.setCourseList(new ArrayList<>());
            existingPanier.setCreatedAt(Instant.now());
            existingPanier.setStatusPanier(StatusPanier.PENDING);
            existingPanier.setUser(user);
        }

        // Step 4: Check if the course is already in the panier
        if (existingPanier.getCourseList().contains(course)) {
            throw new RuntimeException("Course with ID " + courseId + " is already in the panier.");
        }

        // Step 5: Add the course to the panier
        existingPanier.getCourseList().add(course);
        // Step 6: Calculate and set the total amount
        double totalAmount = existingPanier.getCourseList().stream()
                .mapToDouble(Course::getDiscountPrice)
                .sum();
        existingPanier.setTotalAmount(totalAmount);

        // Update the course's panierList (optional bidirectional mapping)
        if (course.getPanierList() == null) {
            course.setPanierList(new ArrayList<>());
        }
        course.getPanierList().add(existingPanier);

        // Step 7: Save the updated panier
        return panierRepository.save(existingPanier);
    }


    @Override
    public Panier removeCourseFromPanier(Long courseId) {
        // Step 1: Get the current user and their PENDING panier
        User user = sessionService.getUserBySession().get();
        Panier panier = panierRepository.findByUserAndStatusPanier(user, StatusPanier.PENDING);

        if (panier == null) {
            throw new RuntimeException("No active panier found for the current user.");
        }

        // Step 2: Check if the course exists in the panier
        Course courseToRemove = panier.getCourseList().stream()
                .filter(course -> Objects.equals(course.getIdCourse(), courseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Course with ID " + courseId + " is not in the panier."));

        // Step 3: Remove the course from the panier
        panier.getCourseList().remove(courseToRemove);

        // Step 6: Calculate and set the total amount
        double totalAmount = panier.getCourseList().stream()
                .mapToDouble(Course::getDiscountPrice)
                .sum();
        panier.setTotalAmount(totalAmount);
        courseToRemove.getPanierList().remove(panier);
        courseRepository.save(courseToRemove);

        // Step 4: Save the updated panier
        return panierRepository.save(panier);
    }

    @Override
    public void clearPanier() {

    }

    @Override
    public Panier getPanier() {
        // Step 1: Get the current user and their PENDING panier
        User user = sessionService.getUserBySession().get();
        Panier panier = panierRepository.findByUserAndStatusPanier(user, StatusPanier.PENDING);

        if (panier == null) {
            return null;
        }

        return panier;
    }


    @Override
    public Double getPaniertotal() {
        return null;
    }
}
