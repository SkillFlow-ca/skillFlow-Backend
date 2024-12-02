package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    @Query("select u from User u where u.email=:v1")
    public Optional<User> findUserByEmail(@Param("v1") String email);
    User findByTokenToValidate(Long tokenToValidate);
    User findByTokenToForgotPassword(Long tokenToForgotPassword);
    @Query("select u from User u where u.isValidated=true")
    Page<User> findActiveUsers(Pageable pageable);
    @Query("select u from User u ")
    Page<User> findUsers(Pageable pageable);
}
